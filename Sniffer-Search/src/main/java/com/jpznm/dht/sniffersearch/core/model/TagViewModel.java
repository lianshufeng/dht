package com.jpznm.dht.sniffersearch.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagViewModel {
    //标签名
    private String name;
    //总量
    private long count;
}
