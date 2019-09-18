package com.jpznm.dht.sniffercore.core.dht;

import com.fast.dev.core.util.bytes.BytesUtil;
import com.jpznm.dht.sniffercore.core.conf.DHTServerConfig;
import com.jpznm.dht.sniffercore.core.dht.coding.BencodingInputStream;
import com.jpznm.dht.sniffercore.core.dht.coding.BencodingOutputStream;
import com.jpznm.dht.sniffercore.core.dht.listener.OnAnnouncePeerListener;
import com.jpznm.dht.sniffercore.core.dht.listener.OnGetPeersListener;
import com.jpznm.dht.sniffercore.core.dht.model.Node;
import com.jpznm.dht.sniffercore.core.dht.model.Queue;
import com.jpznm.dht.sniffercore.core.dht.util.NodeIdUtil;
import com.jpznm.dht.sniffercore.core.dht.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@Component
public class DHTServer {

    @Autowired
    private DHTServerConfig dhtServerConfig;

    /**
     * node id
     */
    private byte[] nodId = createRandomNodeId();

    /**
     * node队列
     */
    private Queue<Node> queue = new Queue<Node>();

    /**
     * 自动重新加入DHT网络 timer
     */
    private Timer autoRejoinDHTTimer;

    /**
     * Netty Channel
     */
    private Channel channel;

    @Autowired
    private OnGetPeersListener onGetPeersListener;

    @Autowired
    private OnAnnouncePeerListener onAnnouncePeerListener;



    private ConnectionlessBootstrap b;


    // 线程池
    private ExecutorService findNodeThreadPool = null;

    // 启动时间
    private long startTime = System.currentTimeMillis();

    // hash的总个数
    private long infoHashCount = 0;


    /**
     * 获取启动节点
     *
     * @return
     */
    private List<InetSocketAddress> getBootstrapNodes() {
        List<InetSocketAddress> ips = new ArrayList<InetSocketAddress>();
        for (String node : this.dhtServerConfig.getRootNodes()) {
            String[] nodeHost = node.split(":");
            InetSocketAddress inetSocketAddress = null;
            if (nodeHost != null && nodeHost.length == 1) {
                inetSocketAddress = new InetSocketAddress(nodeHost[0], 6881);
            } else if (nodeHost.length > 1) {
                inetSocketAddress = new InetSocketAddress(nodeHost[0], Integer.parseInt(nodeHost[1]));
            }
            if (inetSocketAddress != null) {
                ips.add(inetSocketAddress);
            }
        }
        return ips;
    }


    @Autowired
    private void init() {

        DatagramChannelFactory factory = new NioDatagramChannelFactory(Executors.newCachedThreadPool());

        final PacketHandler packetHandler = new PacketHandler();

        b = new ConnectionlessBootstrap(factory);
        b.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(packetHandler);
            }
        });
        b.setOption("receiveBufferSize", 65536);
        b.setOption("sendBufferSize", 268435456);
        channel = b.bind(new InetSocketAddress(this.dhtServerConfig.getPort()));

        // 调度器，当检查到没有节点的时候，则从回默认节点，满血复活
        autoRejoinDHTTimer = new Timer();
        autoRejoinDHTTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (queue.size() <= 0) {
                    joinDHT();
                }
            }
        }, 1000, 5000);

        // 寻找节点线程池
        findNodeThreadPool = Executors.newFixedThreadPool(this.dhtServerConfig.getMaxRunFindNodeCount());

        printLog();
    }


    private void printLog() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long runTime = System.currentTimeMillis() - startTime;
                long s = runTime / 1000;
                BigDecimal count = new BigDecimal(infoHashCount);
                BigDecimal time = new BigDecimal(s);
                double rate = count.divide(time, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(60))
                        .doubleValue();
                String info = String.format("-[ %s ] - [ count : %s ] - [  rate: %s/min ] - [ nodeCount : %s]", TimeUtil.formatTimer(runTime),
                        infoHashCount, rate,queue.size());
                log.info(info);
            }
        }, 1000, 5000);
    }

    private void joinDHT() {
        for (final InetSocketAddress address : getBootstrapNodes()) {
            findNode(address, null, getNodeId());
        }
    }

    private class PacketHandler extends SimpleChannelHandler {
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
            ByteArrayInputStream stream = new ByteArrayInputStream(((ChannelBuffer) e.getMessage()).array());
            BencodingInputStream bencode = new BencodingInputStream(stream);
            try {
                try {
                    Map<String, ?> map = bencode.readMap();
                    if (map != null) {
                        packetProcessing((InetSocketAddress) e.getRemoteAddress(), map);
                    }
                } catch (EOFException eof) {
                    log.error(eof.getMessage(), eof);
                }
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            log.debug(String.format(" - [%s] - [%s]", ctx.getHandler(), e));
        }
    }

    /**
     * 数据包处理
     *
     * @param address 节点地址
     * @param map     数据包 map
     */
    @SuppressWarnings("unchecked")
    private void packetProcessing(InetSocketAddress address, Map<String, ?> map) {
        try {
            Object data = map.get("y");
            if (data != null) {
                String y = new String((byte[]) data);
                if (y.equals("q")) {
                    query(address, (byte[]) map.get("t"), new String((byte[]) map.get("q")),
                            (Map<String, ?>) map.get("a"));
                } else if (y.equals("r")) {
                    response(address, (byte[]) map.get("t"), (Map<String, ?>) map.get("r"));
                }
            }
        } catch (Exception e) {
            log.error(String.format(" -[%s] - [%s] ", e, map));
        }
    }

    /**
     * 查询请求处理
     *
     * @param address 节点地址
     * @param t       transaction id
     * @param q       查询名称：ping、find_node、get_peers、announce_peer中的一种
     * @param a       查询内容
     */
    private void query(InetSocketAddress address, byte[] t, String q, Map<String, ?> a) {
        if (q.equals("ping"))
            responsePing(address, t);
        else if (q.equals("find_node"))
            responseFindNode(address, t);
        else if (q.equals("get_peers")) {
            responseGetPeers(address, t, (byte[]) a.get("info_hash"));
        } else if (q.equals("announce_peer")) {
            if (a.containsKey("implied_port") && ((BigInteger) a.get("implied_port")).intValue() != 0) {
                responseAnnouncePeer(address, t, (byte[]) a.get("info_hash"), address.getPort(),
                        (byte[]) a.get("token"));
            } else {
                responseAnnouncePeer(address, t, (byte[]) a.get("info_hash"), ((BigInteger) a.get("port")).intValue(),
                        (byte[]) a.get("token"));
            }
        }
    }

    /**
     * 回应 ping 请求
     *
     * @param address 节点地址
     * @param t       transaction id
     */
    private void responsePing(InetSocketAddress address, byte[] t) {
        sendKRPC(address, createQueries(t, "r", new HashMap<String, Object>()));
        log.debug("responsePing " + address.toString());
    }

    /**
     * 回应 find_node 请求
     *
     * @param address 节点地址
     * @param t       transaction id
     */
    private void responseFindNode(InetSocketAddress address, byte[] t) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("nodes", new byte[]{});
        sendKRPC(address, createQueries(t, "r", map));
        log.debug("responseFindNode " + address.toString());
    }

    /**
     * 回应 get_peers 请求
     *
     * @param address   节点地址
     * @param t         transaction id
     * @param info_hash torrent's infohash
     */
    private void responseGetPeers(InetSocketAddress address, byte[] t, byte[] info_hash) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("token", new byte[]{info_hash[0], info_hash[1]});
        map.put("nodes", new byte[]{});
        map.put("id", getNeighbor(info_hash));
        sendKRPC(address, createQueries(t, "r", map));
        if (onGetPeersListener != null) {
            try {
                onGetPeersListener.onGetPeers(address, info_hash);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug("info_hash[GetPeers] : " + address.toString() + " - " + BytesUtil.binToHex(info_hash));
        infoHashCount++;
    }

    private byte[] getNeighbor(byte[] info_hash) {
        byte[] bytes = new byte[20];
        System.arraycopy(info_hash, 0, bytes, 0, 10);
        System.arraycopy(getNodeId(), 10, bytes, 10, 10);
        return bytes;
    }

    /**
     * 回应 announce_peer 请求
     *
     * @param address   节点地址
     * @param t         transaction id
     * @param info_hash torrent's infohash
     * @param port      download port
     */
    private void responseAnnouncePeer(InetSocketAddress address, byte[] t, byte[] info_hash, int port, byte[] token) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", getNeighbor(info_hash));
        sendKRPC(address, createQueries(t, "r", map));
        if (Arrays.equals(token, Arrays.copyOfRange(info_hash, 0, 2))) {
            if (onAnnouncePeerListener != null) {
                try {
                    onAnnouncePeerListener.onAnnouncePeer(address, info_hash, port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        log.debug("info_hash[AnnouncePeer] : " + address.toString() + " - " + BytesUtil.binToHex(info_hash));
        infoHashCount++;
    }

    /**
     * 处理答复
     *
     * @param address 节点地址
     * @param t       答复transaction id，由于爬虫每次发出查询将查询名称作为transaction id，因此可以用它来判断答复的类型
     * @param r       答复内容
     */
    private void response(InetSocketAddress address, byte[] t, Map<String, ?> r) {
        if (t == null)
            return;
        String str = new String(t);
        if (str.equals("ping"))
            resultPing(address);
        else if (str.equals("find_node"))
            resultFindNode(address, (byte[]) r.get("nodes"));
        else
            resultGetPeers(address, t, r);
    }

    /**
     * 处理 ping 回应结果
     *
     * @param address
     */
    private void resultPing(InetSocketAddress address) {
        // TODO
    }

    /**
     * 处理 find_node 回应结果
     *
     * @param address 节点地址
     * @param nodes   节点列表 byte 数组
     */
    private void resultFindNode(InetSocketAddress address, byte[] nodes) {
        decodeNodes(nodes);
        log.debug("resultFindNode : " + address.toString());
    }

    /**
     * 处理 get_peers 回应结果(这里我们做爬虫，实际上上不需要实现)
     *
     * @param address
     * @param info_hash
     * @param r
     */
    private void resultGetPeers(InetSocketAddress address, byte[] info_hash, Map<String, ?> r) {

    }

    /**
     * 创建请求数据包
     *
     * @param t   transaction id
     * @param y   数据包类型：查询(q)、答复(r)
     * @param arg 内容
     * @return map
     */
    private Map<String, ?> createQueries(byte[] t, String y, Map<String, Object> arg) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("t", t);
        map.put("y", y);
        if (!arg.containsKey("id"))
            arg.put("id", getNodeId());

        if (y.equals("q")) {
            map.put("q", new String(t));
            map.put("a", arg);
        } else {
            map.put("r", arg);
        }

        return map;
    }

    /**
     * 发送请求
     *
     * @param address 节点地址
     * @param map     数据包map
     */
    private void sendKRPC(InetSocketAddress address, Map<String, ?> map) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             BencodingOutputStream bencode = new BencodingOutputStream(stream)) {
            bencode.writeMap(map);
            channel.write(ChannelBuffers.copiedBuffer(stream.toByteArray()), address);
        } catch (Exception e) {
        }
    }

    /**
     * 解码 nodes
     *
     * @param nodes byte array
     * @return 解码后的 node 地址
     */
    private List<InetSocketAddress> decodeNodes(byte[] nodes) {
        if (nodes == null)
            return null;

        LinkedList<InetSocketAddress> list = new LinkedList<InetSocketAddress>();

        for (int i = 0; i < nodes.length; i += 26) {
            InetAddress ip = null;
            try {
                ip = InetAddress
                        .getByAddress(new byte[]{nodes[i + 20], nodes[i + 21], nodes[i + 22], nodes[i + 23]});
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }

            try {
                InetSocketAddress address = new InetSocketAddress(ip,
                        (0x0000FF00 & (nodes[i + 24] << 8)) | (0x000000FF & nodes[i + 25]));
                list.addFirst(address);

                if (queue.size() <= this.dhtServerConfig.getMaxGoodNodeCount()
                        && !address.getHostString().equals(this.dhtServerConfig.getHostname())) {
                    byte[] nid = new byte[20];
                    System.arraycopy(nodes, i, nid, 0, 20);
                    queue.add(new Node(address.getHostString(), address.getPort(), nid));
                }
                log.debug("setNodes :" + address.toString());
            } catch (IllegalArgumentException ex) {
                log.error("", ex);
            }

        }
        return list;
    }

    private byte[] getNodeId() {
        return this.nodId;
    }

    public void ping(InetSocketAddress address) {
        sendKRPC(address, createQueries("ping".getBytes(), "q", new HashMap<String, Object>()));
        log.debug("Ping : " + address.toString());
    }

    /**
     * 发送 find_node 请求
     *
     * @param address
     * @param target
     */
    private void findNode(InetSocketAddress address, byte[] nid, byte[] target) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("target", target);
        if (nid != null)
            map.put("id", getNeighbor(nid));
        sendKRPC(address, createQueries("find_node".getBytes(), "q", map));
        log.debug("findNode : " + address.toString());
    }

    /**
     * 开始任务
     */
    public void start() {
        for (int i = 0; i < this.dhtServerConfig.getMaxRunFindNodeCount(); i++) {
            runFindNodeTask();
        }
    }

    /**
     * 运行找到节点
     */
    private void runFindNodeTask() {
        this.findNodeThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (queue.size() > 0) {
                        Node node = queue.poll();
                        if (node != null) {
                            findNode(new InetSocketAddress(node.getIp(), node.getPort()), node.getNid(),
                                    NodeIdUtil.buildNodeId());
                        }
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runFindNodeTask();
            }
        });

    }

    public static byte[] createRandomNodeId() {
        Random random = new Random();
        byte[] r = new byte[20];
        random.nextBytes(r);
        return r;
    }


    @PreDestroy
    public void stop() {
        if (findNodeThreadPool != null) {
            findNodeThreadPool.shutdownNow();
        }
        if (channel != null)
            channel.close().awaitUninterruptibly();
        if (b != null)
            b.releaseExternalResources();
        if (autoRejoinDHTTimer != null)
            autoRejoinDHTTimer.cancel();
    }
}
