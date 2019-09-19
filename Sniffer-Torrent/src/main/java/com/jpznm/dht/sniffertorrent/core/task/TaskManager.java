package com.jpznm.dht.sniffertorrent.core.task;

import com.jpznm.dht.sniffertorrent.core.conf.DHTServerConfig;
import com.jpznm.dht.sniffertorrent.core.task.impl.Magnet2Torrent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class TaskManager {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DHTServerConfig dhtServerConfig;


    private ExecutorService poolExecutor;

    @Autowired
    private void init() {
        poolExecutor = Executors.newFixedThreadPool(this.dhtServerConfig.getMaxRunTaskCount());


    }


    /**
     * 执行新任务
     */
    public void runTask() {
        Magnet2Torrent magnet2Torrent = this.applicationContext.getBean(Magnet2Torrent.class);
        this.poolExecutor.execute(magnet2Torrent);
    }


    /**
     * 启动服务
     */
    public void start() {
        //初次开始执行任务
        for (int i = 0; i < this.dhtServerConfig.getMaxRunTaskCount(); i++) {
            runTask();
        }
    }


    @PreDestroy
    private void shutdown() {
        this.poolExecutor.shutdownNow();
    }

}
