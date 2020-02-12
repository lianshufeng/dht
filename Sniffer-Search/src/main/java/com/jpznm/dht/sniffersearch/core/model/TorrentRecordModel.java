package com.jpznm.dht.sniffersearch.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TorrentRecordModel {

    //id
    private String id;

    //文件标题
    private String title;

    //索引
    private String indexed;

    //hash地址
    private String url;

    //创建时间
    private String publishTime;

    //文件总大小
    private String fileTotalSize;

    //文件总数量
    private long fileCount;

    //文件的扩展名
    private Set<String> fileTypes;


}
