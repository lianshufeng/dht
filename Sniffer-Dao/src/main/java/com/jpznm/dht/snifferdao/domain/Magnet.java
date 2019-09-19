package com.jpznm.dht.snifferdao.domain;

import com.fast.dev.data.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @作者 练书锋
 * @时间 2018年2月28日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Magnet extends SuperEntity {

    @Indexed(unique = true)
    private String hash;


    // 访问次数
    @Indexed
    private int accessCount;

    // 更新信息的主机
    @Indexed
    private String updateHost;

    // 获取总次数
    @Indexed
    private long getInfoCount;


}
