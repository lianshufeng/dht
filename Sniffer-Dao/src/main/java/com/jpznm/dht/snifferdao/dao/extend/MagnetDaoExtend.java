package com.jpznm.dht.snifferdao.dao.extend;

import com.jpznm.dht.snifferdao.domain.Magnet;

public interface MagnetDaoExtend {


    void updateHash(String hash, String ip);


    /**
     * 获取一个磁力链
     *
     * @return
     */
    Magnet getOnceMagnet();


}
