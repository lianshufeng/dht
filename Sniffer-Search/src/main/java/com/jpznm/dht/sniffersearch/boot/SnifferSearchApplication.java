package com.jpznm.dht.sniffersearch.boot;

import com.fast.dev.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.jpznm.dht.sniffersearch.core", "com.jpznm.dht.snifferdao"})
public class SnifferSearchApplication extends ApplicationBootSuper {


    public static void main(String[] args) {
        SpringApplication.run(SnifferSearchApplication.class, args);
    }


}
