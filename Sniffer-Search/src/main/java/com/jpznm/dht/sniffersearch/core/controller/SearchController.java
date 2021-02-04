package com.jpznm.dht.sniffersearch.core.controller;

import com.fast.dev.core.util.result.InvokerResult;
import com.jpznm.dht.sniffersearch.core.conf.ZNMConfig;
import com.jpznm.dht.sniffersearch.core.model.TagViewModel;
import com.jpznm.dht.sniffersearch.core.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;



    /**
     * 获取所有的标签
     *
     * @return
     */
    @RequestMapping("getTags")
    public InvokerResult<Object> getTags() {
        return InvokerResult.notNull(searchService.getTags());
    }

    /**
     * 获取所有的标签
     *
     * @return
     */
    @RequestMapping("getHotWords")
    public InvokerResult<Object> getHotWords(@PageableDefault(sort = {"count"}, direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        return InvokerResult.notNull(searchService.getHotWords(pageable));
    }


    /**
     * 搜索入口
     *
     * @param keyword
     * @param pageable
     * @return
     */
    @RequestMapping("search")
    public InvokerResult<Object> search(String keyword, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return InvokerResult.notNull(this.searchService.query(keyword, pageable));
    }


}
