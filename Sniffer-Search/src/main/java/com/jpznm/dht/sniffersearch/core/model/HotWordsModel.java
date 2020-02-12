package com.jpznm.dht.sniffersearch.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotWordsModel {

    //标签名
    private String name;

    //总数量
    private long count;
}
