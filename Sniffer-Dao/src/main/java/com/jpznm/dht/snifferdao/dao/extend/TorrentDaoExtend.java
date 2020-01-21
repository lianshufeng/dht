package com.jpznm.dht.snifferdao.dao.extend;

import com.jpznm.dht.snifferdao.domain.Torrent;

import java.util.List;

public interface TorrentDaoExtend {


    /**
     * 更新
     *
     * @param torrent
     */
    void update(Torrent torrent);


    /**
     * 获取种子文件
     *
     * @param size
     * @return
     */
    List<Torrent> getOnceTorrent(int size);


}
