package com.jpznm.dht.snifferdao.dao.extend;

import com.jpznm.dht.snifferdao.domain.Magnet;

import java.util.List;

public interface MagnetDaoExtend {


    void put(String hash, String ip);


    List<Magnet> getMagnet(int size);

}
