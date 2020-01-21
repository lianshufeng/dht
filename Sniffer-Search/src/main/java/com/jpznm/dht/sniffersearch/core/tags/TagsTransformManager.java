package com.jpznm.dht.sniffersearch.core.tags;

import com.jpznm.dht.sniffersearch.core.conf.ZNMConfig;
import com.jpznm.dht.sniffersearch.core.model.TagsModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@Component
public class TagsTransformManager {

    @Autowired
    private ZNMConfig znmConfig;

    private Map<String, Set<String>> tags = new ConcurrentHashMap<>();
    private Map<String, TagsTransform> tagsTransformMap = new ConcurrentHashMap<>();

    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    @SneakyThrows
    private void init() {
        tags.clear();
        tagsTransformMap.clear();


        //加载配置到缓存里
        for (TagsModel it : znmConfig.getTags()) {

            String tagName = it.getName();

            //标签
            if (it.getTags() != null) {
                tags.put(tagName, it.getTags());
            }


            //加载处理标签的class
            if (it.getCls() != null) {
                tagsTransformMap.put(tagName, (TagsTransform) applicationContext.getBean(it.getCls()));
            }
        }


    }


    /**
     * 获取所有的tags
     *
     * @param infos
     * @return
     */
    public Set<String> getTags(String... infos) {
        Set<String> ret = new HashSet<>();
        for (String info : infos) {
            ret.addAll(getTags(info));
        }
        return ret;
    }


    /**
     * 获取标签
     *
     * @param info
     * @return
     */
    private Set<String> getTags(String info) {
        Set<String> ret = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : this.tags.entrySet()) {

            //配置标签
            ret.addAll(entry.getValue().stream().filter((it) -> {
                return info.indexOf(it) > -1;
            }).map((it) -> {
                return entry.getKey();
            }).collect(Collectors.toSet()));


            //class的动态判断
            TagsTransform tagsTransform = tagsTransformMap.get(entry.getKey());
            if (tagsTransform != null) {
                if (tagsTransform.execute(info)) {
                    ret.add(entry.getKey());
                }
            }


        }


        //配置class


        return ret;
    }


}
