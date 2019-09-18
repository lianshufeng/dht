package com.jpznm.dht.sniffercore.boot;

import com.fast.dev.core.boot.ApplicationBootSuper;
import com.jpznm.dht.sniffercore.core.dht.DHTServer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.jpznm.dht.sniffercore.core")
public class SniffercoreApplication extends ApplicationBootSuper {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SniffercoreApplication.class, args);
        //启动服务
        applicationContext.getBean(DHTServer.class).start();

    }

}
