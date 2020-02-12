package com.jpznm.dht.sniffersearch.boot;

import com.fast.dev.core.boot.ApplicationBootSuper;
import com.fast.dev.core.mvc.MVCConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@Import(MVCConfiguration.class)
@ComponentScan({"com.jpznm.dht.sniffersearch.core", "com.jpznm.dht.snifferdao"})
public class SnifferSearchApplication extends ApplicationBootSuper {


    public static void main(String[] args) {
        SpringApplication.run(SnifferSearchApplication.class, args);
    }


}
