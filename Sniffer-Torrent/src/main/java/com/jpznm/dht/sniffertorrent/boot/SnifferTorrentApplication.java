package com.jpznm.dht.sniffertorrent.boot;

import com.fast.dev.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.jpznm.dht.sniffertorrent.core", "com.jpznm.dht.snifferdao"})
public class SnifferTorrentApplication extends ApplicationBootSuper {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SnifferTorrentApplication.class, args);


    }

}
