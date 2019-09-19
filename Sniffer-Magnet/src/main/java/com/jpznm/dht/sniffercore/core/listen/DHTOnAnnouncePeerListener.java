package com.jpznm.dht.sniffercore.core.listen;

import com.fast.dev.core.util.bytes.BytesUtil;
import com.jpznm.dht.sniffercore.core.dht.handler.AnnouncePeerInfoHashWireHandler;
import com.jpznm.dht.sniffercore.core.dht.listener.OnAnnouncePeerListener;
import com.jpznm.dht.sniffercore.core.dht.listener.OnMetadataListener;
import com.jpznm.dht.sniffercore.core.dht.model.Info;
import com.jpznm.dht.sniffercore.core.dht.model.SubFile;
import com.jpznm.dht.sniffercore.core.dht.model.Torrent;
import com.jpznm.dht.sniffercore.core.util.AddressUtil;
import com.jpznm.dht.snifferdao.dao.MagnetDao;
import com.jpznm.dht.snifferdao.dao.TorrentDao;
import com.jpznm.dht.snifferdao.model.FileModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * AnnouncePeer
 *
 * @作者 练书锋
 * @时间 2018年2月28日
 */
@Slf4j
@Service
public class DHTOnAnnouncePeerListener implements OnAnnouncePeerListener {

    @Autowired
    private MagnetDao magnetDao;

    @Autowired
    private TorrentDao torrentDao;


    @Override
    public void onAnnouncePeer(InetSocketAddress address, byte[] info_hash, int port) {
        String ip = address.getAddress().getHostAddress();
        String hash = BytesUtil.binToHex(info_hash).toLowerCase();
        log.info(String.format("[AnnouncePeer] - %s:%s - %s", ip, port, hash));
        magnetDao.updateHash(hash, AddressUtil.format(address));

        AnnouncePeerInfoHashWireHandler handler = new AnnouncePeerInfoHashWireHandler();
        initHandler(handler);
        try {
            handler.handler(new InetSocketAddress(ip, port), info_hash);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

    private void initHandler(AnnouncePeerInfoHashWireHandler handler) {
        handler.setOnMetadataListener(new OnMetadataListener() {
            @Override
            public void onMetadata(Torrent torrent) {
                if (torrent == null || torrent.getInfo() == null)
                    return;
                // 入库操作
                String hash = torrent.getInfo_hash().trim().toLowerCase();

                com.jpznm.dht.snifferdao.domain.Torrent torrentEnity = new com.jpznm.dht.snifferdao.domain.Torrent();
                torrentEnity.setHash(hash);
                if (torrent.getCreationDate() == null) {
                    torrentEnity.setCreationTime(System.currentTimeMillis());
                } else {
                    torrentEnity.setCreationTime(torrent.getCreationDate().getTime());
                }
                Info info = torrent.getInfo();
                if (info != null) {
                    torrentEnity.setName(info.getName());
                    long size = 0;
                    List<FileModel> files = new ArrayList<>();
                    if (info.getFiles() != null && info.getFiles().size() > 0) {
                        for (SubFile torrentFile : info.getFiles()) {
                            long fileSize = Long.parseLong(torrentFile.getLength());
                            files.add(new FileModel(torrentFile.getPath(), fileSize));
                            size += fileSize;
                        }
                    } else {
                        size = info.getLength();
                        files.add(new FileModel(info.getName(), size));
                    }
                    torrentEnity.setSize(size);
                    torrentEnity.setFiles(files.toArray(new FileModel[files.size()]));
                }
                torrentDao.update(torrentEnity);
            }
        });
    }

}
