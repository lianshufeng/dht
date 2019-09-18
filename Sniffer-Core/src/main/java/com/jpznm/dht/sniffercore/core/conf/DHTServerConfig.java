package com.jpznm.dht.sniffercore.core.conf;

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


    //主机名
    private String hostname = "0.0.0.0";

    // 默认端口
    private int port = 6882;

    // 默认的路由节点点
    private final String[] RootNodes = new String[]{"router.bittorrent.com:6881", "dht.transmissionbt.com:6881",
            "router.utorrent.com:6881", "router.bitcomet.com:6881", "dht.aelitis.com:6881"};

    // 最大的优秀节点数量
    private int maxGoodNodeCount = 2000;

    // 最大并行找节点数
    private int maxRunFindNodeCount = 10;


}
