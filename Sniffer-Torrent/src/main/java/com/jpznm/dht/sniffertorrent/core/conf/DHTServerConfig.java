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
    private int maxRunTaskCount = 200;

    //任务超时
    private long taskTimeout = 180000;


}
