package com.jpznm.dht.sniffertorrent.boot;

import com.fast.dev.core.boot.ApplicationBootSuper;
import com.jpznm.dht.sniffertorrent.core.task.TaskManager;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.InputStream;

@ComponentScan({"com.jpznm.dht.sniffertorrent.core", "com.jpznm.dht.snifferdao"})
public class SnifferTorrentApplication extends ApplicationBootSuper {


    private final static String LibtorrentVersion = "1.2.11.0";


    public static void main(String[] args) {
        loadLibs();
        ApplicationContext applicationContext = SpringApplication.run(SnifferTorrentApplication.class, args);
        applicationContext.getBean(TaskManager.class).start();
    }


    /**
     * 载入支持库
     */
    private static void loadLibs() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            addLibs("jlibtorrent-" + LibtorrentVersion + ".dll");
        } else {
            addLibs("libjlibtorrent-" + LibtorrentVersion + ".so");
        }
    }


    /**
     * 加载支持库
     *
     * @param resourceName
     */
    @SneakyThrows
    private static void addLibs(String resourceName) {
        //解压文件
        String path = new ApplicationHome(SnifferTorrentApplication.class).getDir().getAbsolutePath();
        File file = new File(path + "/libs/" + resourceName);
        if (!file.exists()) {
            @Cleanup InputStream inputStream = SnifferTorrentApplication.class.getClassLoader().getResourceAsStream("/libs/" + resourceName);
            FileUtils.copyInputStreamToFile(inputStream, file);
        }

        //加载支持库
        System.setProperty("jlibtorrent.jni.path", file.getAbsolutePath());

    }


}
