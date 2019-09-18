package com.jpznm.dht.sniffercore.core.listen;

import com.fast.dev.core.util.bytes.BytesUtil;
import com.jpznm.dht.sniffercore.core.dao.InfoHashDao;
import com.jpznm.dht.sniffercore.core.dht.listener.OnGetPeersListener;
import com.jpznm.dht.sniffercore.core.util.AddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;
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
    private InfoHashDao infoHashDao;


    @Override
    public void onGetPeers(InetSocketAddress address, byte[] info_hash) {
        String hash = BytesUtil.binToHex(info_hash).toLowerCase();
        log.debug("hash : " + hash);
        // 保存到数据库里
        infoHashDao.updateHash(hash, AddressUtil.format(address));


    }

}
