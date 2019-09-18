package com.jpznm.dht.sniffercore.core.impl.listen;

import com.fast.dev.core.util.bytes.BytesUtil;
import com.jpznm.dht.sniffercore.core.dht.handler.AnnouncePeerInfoHashWireHandler;
import com.jpznm.dht.sniffercore.core.dht.listener.OnAnnouncePeerListener;
import com.jpznm.dht.sniffercore.core.dht.listener.OnMetadataListener;
import com.jpznm.dht.sniffercore.core.dht.model.Torrent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * AnnouncePeer
 *
 * @作者 练书锋
 * @时间 2018年2月28日
 */
@Slf4j
@Service
public class DHTOnAnnouncePeerListener implements OnAnnouncePeerListener {


    @Override
    public void onAnnouncePeer(InetSocketAddress address, byte[] info_hash, int port) {
        String ip = address.getAddress().getHostAddress();
        String hash = BytesUtil.binToHex(info_hash).toLowerCase();
        log.info(String.format("[AnnouncePeer] - %s:%s - %s", ip, port, hash));
//		infoHashDao.updateHash(hash, AddressUtil.format(address));
//		if (!torrentDao.exists(hash, com.fast.dht.db.domain.Torrent.class)) {
//			AnnouncePeerInfoHashWireHandler handler = new AnnouncePeerInfoHashWireHandler();
//			initHandler(handler);
//			try {
//				handler.handler(new InetSocketAddress(ip, port), info_hash);
//			} catch (Exception e) {
//				LOG.error(e);
//			}
//		}
    }

    private void initHandler(AnnouncePeerInfoHashWireHandler handler) {
        // private void initHandler() {
        handler.setOnMetadataListener(new OnMetadataListener() {
            @Override
            public void onMetadata(Torrent torrent) {
                if (torrent == null || torrent.getInfo() == null)
                    return;
                // 入库操作
                String hash = torrent.getInfo_hash().trim().toLowerCase();

//                com.fast.dht.db.domain.Torrent torrentEnity = new com.fast.dht.db.domain.Torrent();
//                torrentEnity.setHash(hash);
//                if (torrent.getCreationDate() == null) {
//                    torrentEnity.setCreationTime(System.currentTimeMillis());
//                } else {
//                    torrentEnity.setCreationTime(torrent.getCreationDate().getTime());
//                }
//                Info info = torrent.getInfo();
//                if (info != null) {
//                    torrentEnity.setName(info.getName());
//                    long size = 0;
//                    List<FileModel> files = new ArrayList<>();
//                    if (info.getFiles() != null && info.getFiles().size() > 0) {
//                        for (SubFile torrentFile : info.getFiles()) {
//                            long fileSize = Long.parseLong(torrentFile.getLength());
//                            files.add(new FileModel(torrentFile.getPath(), fileSize));
//                            size += fileSize;
//                        }
//                    } else {
//                        size = info.getLength();
//                        files.add(new FileModel(info.getName(), size));
//                    }
//                    torrentEnity.setSize(size);
//                    torrentEnity.setFiles(files.toArray(new FileModel[files.size()]));
//                }
//                torrentDao.update(torrentEnity);
            }
        });
    }

}
