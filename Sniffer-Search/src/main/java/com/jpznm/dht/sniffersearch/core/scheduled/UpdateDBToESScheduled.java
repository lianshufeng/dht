package com.jpznm.dht.sniffersearch.core.scheduled;

import com.jpznm.dht.sniffersearch.core.service.UpdateESIndexService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 同步数据到es中
 */
@EnableScheduling
@Component
@Log
public class UpdateDBToESScheduled {

    @Autowired
    private UpdateESIndexService updateESIndexService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void update() {
        this.updateESIndexService.update();
    }


}
