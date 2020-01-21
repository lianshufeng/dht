package com.jpznm.dht.sniffersearch.core.service;

import com.jpznm.dht.snifferdao.dao.TorrentDao;
import com.jpznm.dht.snifferdao.domain.Torrent;
import com.jpznm.dht.sniffersearch.core.conf.ZNMConfig;
import com.jpznm.dht.sniffersearch.core.dao.TorrentRecordDao;
import com.jpznm.dht.sniffersearch.core.domain.TorrentRecord;
import com.jpznm.dht.sniffersearch.core.tags.TagsTransformManager;
import com.jpznm.dht.sniffersearch.core.util.SpliteUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log
@Service
public class UpdateESIndexService {


    @Autowired
    private ZNMConfig znmConfig;

    @Autowired
    private TorrentDao torrentDao;

    @Autowired
    private TagsTransformManager tagsTransformManager;

    @Autowired
    private TorrentRecordDao torrentRecordDao;


    /**
     * 同步数据到es中
     */
    public void update() {

        //获取需要更新的数据
        List<Torrent> items = this.torrentDao.getOnceTorrent(znmConfig.getMaxUpdateCount());
        if (items.size() <= 0) {
            return;
        }

        log.info("载入数据 : " + items.size());

        //
        Set<TorrentRecord> torrentRecordSet = new HashSet<>();
        for (Torrent item : items) {

            Set<String> infos = new HashSet<>();

            //文件名
            infos.add(item.getName());

            //文件内容
            List<String> files = Arrays.asList(item.getFiles()).stream().map((it) -> {
                return it.getPath();
            }).collect(Collectors.toList());
            infos.addAll(files);

            //标签
            Set<String> tags = tagsTransformManager.getTags(infos.toArray(new String[0]));
            if (tags.size() > 0) {
                infos.addAll(tags.stream().map((it) -> {
                    return String.format("[tag:%s]", it);
                }).collect(Collectors.toSet()));
            }

            //同步到es的语法
            TorrentRecord torrentRecord = new TorrentRecord();

            torrentRecord.setTitle(item.getName());

            torrentRecord.setIndexed(indexHandle(infos));
            torrentRecord.setUrl(String.format("magnet:?xt=urn:btih:%s", item.getHash()));

            torrentRecord.setCreateTime(item.getCreateTime());
            torrentRecord.setUpdateTime(item.getUpdateTime());


            torrentRecordSet.add(torrentRecord);
        }

        if (torrentRecordSet.size() > 0) {
            torrentRecordDao.saveAll(torrentRecordSet);
        }


    }

    /**
     * 构建索引
     *
     * @param infos
     * @return
     */
    private String indexHandle(Set<String> infos) {
        Set<String> ret = new HashSet<>();
        for (String info : infos) {
            ret.addAll(Arrays.asList(SpliteUtil.getToken(info)));
        }
        return String.join(" ", ret);
    }

    /**
     * 获取时间
     *
     * @return
     */
    private long getTime() {
        return System.currentTimeMillis();
    }


}
