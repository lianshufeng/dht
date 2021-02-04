package com.jpznm.dht.sniffersearch.core.tags;

import java.util.Set;

/**
 * 标记处理器
 */

@FunctionalInterface
public interface TagsTransform {

    /**
     * 解析函数
     *
     * @param info
     * @return
     */
    boolean execute(String info);

}
