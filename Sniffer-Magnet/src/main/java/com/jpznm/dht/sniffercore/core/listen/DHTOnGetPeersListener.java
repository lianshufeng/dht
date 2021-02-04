package com.jpznm.dht.sniffercore.core.listen;

import com.fast.dev.core.util.bytes.BytesUtil;
import com.jpznm.dht.sniffercore.core.dht.listener.OnGetPeersListener;
import com.jpznm.dht.sniffercore.core.util.AddressUtil;
import com.jpznm.dht.snifferdao.dao.MagnetDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * GetPeer
 *
 * @作者 练书锋
 * @时间 2018年2月28日
 */

@Slf4j
@Service
public class DHTOnGetPeersListener implements OnGetPeersListener {

    @Autowired
    private MagnetDao magnetDao;


    @Override
    public void onGetPeers(InetSocketAddress address, byte[] info_hash) {
        String hash = BytesUtil.binToHex(info_hash).toLowerCase();
        log.debug("hash : " + hash);
        // 保存到数据库里
        magnetDao.put(hash, AddressUtil.format(address));

    }

}
