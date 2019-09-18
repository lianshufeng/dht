package com.jpznm.dht.sniffercore.core.domain;

import com.fast.dev.data.mongo.domain.SuperEntity;
import com.jpznm.dht.sniffercore.core.model.FileModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Torrent extends SuperEntity {

    @Indexed(unique = true)
    private String hash;

    // 种子创建时间
    @Indexed
    private long creationTime;

    // 种子的名称
    @Indexed
    private String name;

    // 文件列表
    private FileModel[] files;

    // 占用空间
    private long size;

}
