package com.jpznm.dht.sniffertorrent.core.service;

import com.jpznm.dht.snifferdao.dao.MagnetDao;
import com.jpznm.dht.snifferdao.domain.Magnet;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Log
@Service
public class MagnetService {


    private final static int getDataSize = 500;

    @Autowired
    private MagnetDao magnetDao;


    //磁力缓存池
    private Vector<Magnet> magnetCachePool = new Vector<>();


    /**
     * 获取一条记录
     *
     * @return
     */
    @SneakyThrows
    public synchronized Magnet getOnecMagnet() {
        //如果池中有，则直接返回
        if (magnetCachePool.size() > 0) {
            return magnetCachePool.remove(0);
        }

        //阻塞并等待db请求到数据
        CountDownLatch countDownLatch = new CountDownLatch(1);


        //检查是否查询完成
        checkQueryFinish(countDownLatch);


        //开始同步数据
        threadQueryMagnet();


        countDownLatch.await();
        return magnetCachePool.remove(0);
    }

    /**
     * 是否可阻塞
     *
     * @param countDownLatch
     */
    private void checkQueryFinish(CountDownLatch countDownLatch) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (magnetCachePool.size() > 0) {
                    countDownLatch.countDown();
                    timer.cancel();
                }
            }
        }, 300, 300);
    }


    //同步到数据
    private void threadQueryMagnet() {
        //启动更新数据的线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Magnet> list = magnetDao.getMagnet(getDataSize);
                log.info("更新数据: " + list.size());
                if (list != null && list.size() > 0) {
                    magnetCachePool.addAll(list);
                }
            }
        }).start();
    }


}
