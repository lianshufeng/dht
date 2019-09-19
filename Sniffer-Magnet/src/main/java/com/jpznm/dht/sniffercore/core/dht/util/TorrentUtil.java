package com.jpznm.dht.sniffercore.core.dht.util;

import com.fast.dev.core.util.JsonUtil;
import com.jpznm.dht.sniffercore.core.dht.handler.AnnouncePeerInfoHashWireHandler;
import com.jpznm.dht.sniffercore.core.dht.listener.OnMetadataListener;
import com.jpznm.dht.sniffercore.core.dht.model.Torrent;
import com.jpznm.dht.sniffercore.core.dht.model.TorrentFile;

import java.net.InetSocketAddress;
import java.util.Date;

public class TorrentUtil {

    public static void convertTorrent(String ip, int port, byte[] info_hash) throws Exception {
        AnnouncePeerInfoHashWireHandler handler = new AnnouncePeerInfoHashWireHandler();
        initHandler(handler);
        handler.handler(new InetSocketAddress(ip, port), info_hash);
    }

    private static void initHandler(AnnouncePeerInfoHashWireHandler handler) {
        // private void initHandler() {
        handler.setOnMetadataListener(new OnMetadataListener() {
            @Override
            public void onMetadata(Torrent torrent) {
                // System.out.println("finished,dps size:" + dps.size());
                if (torrent == null || torrent.getInfo() == null)
                    return;
                // 入库操作

                try {
                    TorrentFile tFile = new TorrentFile();
                    tFile.setInfoHash(torrent.getInfo_hash());
                    tFile.setName(torrent.getInfo().getName());
                    tFile.setSize(torrent.getInfo().getLength());
                    tFile.setType(torrent.getType());
                    tFile.setFindDate(new Date());
                    tFile.setHot(1);
                    tFile.setSubfiles(JsonUtil.toJson(torrent.getInfo().getFiles()));
                    System.out.println("转换OK：" + JsonUtil.toJson(tFile));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
