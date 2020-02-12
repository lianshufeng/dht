package com.jpznm.dht.snifferdao.domain;

import com.fast.dev.data.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class HotWords extends SuperEntity {

    /**
     * 标签名
     */
    @Indexed(unique = true)
    private String name;

    //总数量
    @Indexed
    private long count;


}
