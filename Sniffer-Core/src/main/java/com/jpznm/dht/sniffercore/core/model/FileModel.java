package com.jpznm.dht.sniffercore.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件模型
 *
 * @作者 练书锋
 * @时间 2018年3月3日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileModel {

    private String path;

    private long size;


}
