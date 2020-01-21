package com.jpznm.dht.sniffertorrent.core.helper;

import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.SessionParams;
import com.frostwire.jlibtorrent.SettingsPack;
import com.frostwire.jlibtorrent.swig.settings_pack;
import com.jpznm.dht.sniffertorrent.core.conf.DHTServerConfig;
import com.jpznm.dht.sniffertorrent.core.torrent.util.TorrentReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class DHTHelper {

    @Autowired
    private DHTServerConfig dhtServerConfig;


    SessionManager sessionManager;
    SessionParams params;

    long currentTotalNode = 0;

    @Autowired
    private void init() {
        sessionManager = new SessionManager();
        SettingsPack sp = new SettingsPack();

		for (String rootNode : this.dhtServerConfig.getRootNodes()) {
			sp.setString(settings_pack.string_types.dht_bootstrap_nodes.swigValue(), rootNode);
		}

        params = new SessionParams(sp);
        sessionManager.start(params);

        waitMoreNodes();

    }

    /**
     * 请求种子文件
     */
    public TorrentReader query(final String hash) {
        String magnet = createMagnet(hash);
        return queryTask(magnet);
    }

    private TorrentReader queryTask(final String magnet) {
        log.debug(String.format("request : %s ", magnet));
        byte[] data = this.sessionManager.fetchMagnet(magnet, (int) (this.dhtServerConfig.getTaskTimeout() / 1000));
        log.debug(String.format(" [ %s ]   : %s", magnet, data != null));
        if (data == null) {
            return null;
        }
        try {
            return new TorrentReader(data, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建磁力连
     *
     * @return
     */
    private String createMagnet(String hash) {
        return String.format("magnet:?xt=urn:btih:%s", hash);
    }

    private void waitMoreNodes() {
        long nodes = sessionManager.stats().dhtNodes();
        if (nodes < this.dhtServerConfig.getMinNodesCount()) {
            final CountDownLatch signal = new CountDownLatch(1);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    long nodes = sessionManager.stats().dhtNodes();
                    if (currentTotalNode != nodes) {
                        currentTotalNode = nodes;
                        log.info(String.format("dhtNodes : %s", currentTotalNode));
                    }
                    if (nodes >= dhtServerConfig.getMinNodesCount()) {
                        if (signal.getCount() != 0) {
                            signal.countDown();
                        }
                    }
                }
            }, 0, 1000);
            log.info("wait more node ....");
            try {
                signal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
