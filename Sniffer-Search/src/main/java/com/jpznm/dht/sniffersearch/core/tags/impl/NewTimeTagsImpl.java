package com.jpznm.dht.sniffersearch.core.tags.impl;

import com.jpznm.dht.sniffersearch.core.tags.TagsTransform;
import org.springframework.stereotype.Component;

@Component
public class NewTimeTagsImpl implements TagsTransform {


    @Override
    public boolean execute(String info) {
        return false;
    }
}
