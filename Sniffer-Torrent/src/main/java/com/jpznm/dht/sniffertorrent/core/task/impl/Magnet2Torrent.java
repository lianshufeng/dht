package com.jpznm.dht.sniffertorrent.core.task.impl;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.jpznm.dht.snifferdao.dao.MagnetDao;
import com.jpznm.dht.snifferdao.domain.Magnet;
import com.jpznm.dht.sniffertorrent.core.conf.DHTServerConfig;
import com.jpznm.dht.sniffertorrent.core.helper.DHTHelper;
import com.jpznm.dht.sniffertorrent.core.task.TaskManager;
import com.jpznm.dht.sniffertorrent.core.torrent.util.TorrentReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class Magnet2Torrent implements Runnable {


    @Autowired
    private TaskManager taskManager;

    @Autowired
    private DHTServerConfig dhtServerConfig;

    @Autowired
    private MagnetDao magnetDao;

    @Autowired
    private DHTHelper dhtHelper;


    @Override
    public void run() {
        log.info("开始执行任务:-->");
        // 执行任务
        try {
            task(getHash());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // 下一个任务
            next();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void next() throws InterruptedException {
        Thread.sleep(dhtServerConfig.getTaskSleepTime());
        taskManager.runTask();
    }


    /**
     * 开始任务
     */
    private void task(String hash) {
        TorrentReader torrentReader = this.dhtHelper.query(hash);

        System.out.println(torrentReader);


    }

    /**
     * 获取磁力连接的hash
     *
     * @return
     */
    private String getHash() {
        Magnet magnet = this.magnetDao.getOnceMagnet();
        if (magnet == null) {
            return null;
        }
        return magnet.getHash();
    }

}
