package com.jpznm.dht.sniffersearch.core.controller;

import com.fast.dev.core.util.result.InvokerResult;
import com.jpznm.dht.sniffersearch.core.conf.ZNMConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("s")
public class SearchController {

    @Autowired
    private ZNMConfig znmConfig;

    @RequestMapping("getTags")
    public Object getTags() {
        return InvokerResult.notNull(
                znmConfig.getTags().stream().map((it)->{
            return it.getName();
        }));
    }


}
