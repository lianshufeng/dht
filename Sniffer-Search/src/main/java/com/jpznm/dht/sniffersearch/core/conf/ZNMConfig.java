package com.jpznm.dht.sniffersearch.core.conf;

import com.jpznm.dht.sniffersearch.core.model.TagsModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties("znm")
public class ZNMConfig {

    //最大同步数量
    private int maxUpdateCount = 300;


    //标记
    private List<TagsModel> tags;


}
