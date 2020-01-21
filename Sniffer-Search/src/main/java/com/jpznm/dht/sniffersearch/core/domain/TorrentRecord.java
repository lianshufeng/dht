package com.jpznm.dht.sniffersearch.core.domain;


import com.fast.dev.data.jpa.es.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "torrentrecords", type = "post")
public class TorrentRecord extends SuperEntity {


    //文件标题
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", index = true, type = FieldType.Text)
    private String title;


    //标签
    @Field(index = true, type = FieldType.Text)
    private String indexed;
    

    //hash地址
    @Field(index = true, type = FieldType.Text)
    private String url;

}
