package com.jpznm.dht.sniffertorrent.core.task.impl;

import com.fast.dev.core.util.bytes.BytesUtil;
import com.jpznm.dht.snifferdao.dao.MagnetDao;
import com.jpznm.dht.snifferdao.dao.TorrentDao;
import com.jpznm.dht.snifferdao.domain.Magnet;
import com.jpznm.dht.snifferdao.domain.Torrent;
import com.jpznm.dht.snifferdao.model.FileModel;
import com.jpznm.dht.sniffertorrent.core.conf.DHTServerConfig;
import com.jpznm.dht.sniffertorrent.core.helper.DHTHelper;
import com.jpznm.dht.sniffertorrent.core.task.TaskManager;
import com.jpznm.dht.sniffertorrent.core.torrent.util.TorrentReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private TorrentDao torrentDao;

    @Autowired
    private DHTHelper dhtHelper;


    @Override
    public void run() {
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
        Thread.sleep(getRandom(dhtServerConfig.getTaskSleepTime(), dhtServerConfig.getTaskSleepTime() * 2));
        taskManager.runTask();
    }

    /**
     * 取随机数
     *
     * @param min
     * @param max
     * @return
     */
    private long getRandom(long min, long max) {
        return new Random().nextInt(Integer.parseInt(String.valueOf(max)) - Integer.parseInt(String.valueOf(min)) + 1) + min;
    }


    /**
     * 开始任务
     */
    @SneakyThrows
    private void task(String hash) {
        log.info("开始执行任务: " + hash);
        TorrentReader reader = this.dhtHelper.query(hash);
        //转换为表单类型
        if (reader == null) {
            return;
        }

        log.info("save : " + reader.getName());

        String magnetHash = BytesUtil.binToHex(reader.getInfoHash()).toLowerCase();
        Torrent torrent = new Torrent();
        torrent.setHash(magnetHash);
        if (reader.getCreationDate() != null) {
            torrent.setCreationTime(reader.getCreationDate().getTime());
        }
        torrent.setName(reader.getName());
        List<FileModel> files = new ArrayList<>();
        long size = 0;
        for (TorrentReader.TorrentFile torrentFile : reader.getFiles()) {
            String fileName = torrentFile.file.getPath();
            files.add(new FileModel(fileName, torrentFile.size));
            size += torrentFile.size;
        }
        torrent.setFiles(files.toArray(new FileModel[files.size()]));
        torrent.setSize(size);

        this.torrentDao.update(torrent);
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
