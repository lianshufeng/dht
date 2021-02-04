package com.jpznm.dht.sniffersearch.core.domain;


import com.fast.dev.data.jpa.es.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "torrentrecords", type = "post")
@Setting(settingPath = "es/ES-TorrentRecord-Config.json")
public class TorrentRecord extends SuperEntity {


    //文件标题
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", index = true, type = FieldType.Text)
    private String title;


    //标签
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", index = true, type = FieldType.Text)
    private String indexed;

    //hash地址
    @Field(index = true, type = FieldType.Text)
    private String url;


    //标签
    private Set<String> tags;

    //文件总大小
    private long fileSize;

    //文件总数量
    private long fileCount;

    //文件的扩展名
    private Set<String> fileTypes;


}
