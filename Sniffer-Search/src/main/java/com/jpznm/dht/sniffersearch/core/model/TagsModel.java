package com.jpznm.dht.sniffersearch.core.model;

import com.jpznm.dht.sniffersearch.core.tags.TagsTransform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class TagsModel {

    //名称
    private String name;

    //标记
    private Set<String> tags;

    //处理器
    private Class<? extends TagsTransform> cls;


}
