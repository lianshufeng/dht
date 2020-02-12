package com.jpznm.dht.snifferdao.dao.extend;

import java.util.Collection;
import java.util.Map;

public interface TagRecordDaoExtend {

    /**
     * 标签数量+1
     *
     * @param name
     */
    void inc(Collection<String> name);


    /**
     * 批量增加
     *
     * @param items
     */
    void inc(Map<String, Integer> items);


}
