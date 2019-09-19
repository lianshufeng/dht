package com.jpznm.dht.sniffertorrent.core.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * DHT 服务的配置
 *
 * @作者 练书锋
 * @时间 2018年2月27日
 */

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "dht")
public class DHTServerConfig {

    //最大的任务数量
    private int maxRunTaskCount = 50;

    //任务执行完后休息时间
    private long taskSleepTime = 1000l;

    //任务超时
    private long taskTimeout = 60000;

    //等待最小的节点数量
    private long minNodesCount = 10;

    // 默认的路由节点点
    private  String[] rootNodes = new String[]{"router.bittorrent.com:6881", "dht.transmissionbt.com:6881",
            "router.utorrent.com:6881", "router.bitcomet.com:6881", "dht.aelitis.com:6881"};


}
