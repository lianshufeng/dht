package com.jpznm.dht.sniffersearch.core.conf;

import com.fast.dev.data.jpa.es.config.ElasticsearchConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@Import(ElasticsearchConfiguration.class)
@EnableElasticsearchRepositories("com.jpznm.dht.sniffersearch.core.dao")
public class ESJpaConfig {
}
