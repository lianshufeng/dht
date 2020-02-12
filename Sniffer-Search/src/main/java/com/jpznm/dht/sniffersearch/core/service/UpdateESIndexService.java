package com.jpznm.dht.sniffersearch.core.service;

import com.jpznm.dht.snifferdao.dao.TagRecordDao;
import com.jpznm.dht.snifferdao.dao.TorrentDao;
import com.jpznm.dht.snifferdao.domain.Torrent;
import com.jpznm.dht.snifferdao.model.FileModel;
import com.jpznm.dht.sniffersearch.core.conf.ZNMConfig;
import com.jpznm.dht.sniffersearch.core.dao.TorrentRecordDao;
import com.jpznm.dht.sniffersearch.core.domain.TorrentRecord;
import com.jpznm.dht.sniffersearch.core.tags.TagsTransformManager;
import com.jpznm.dht.sniffersearch.core.util.SpliteUtil;
import lombok.extern.java.Log;
import org.apache.commons.io.FilenameUtils;
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

    @Autowired
    private TagRecordDao tagRecordDao;


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

        //记录标签数量
        Map<String, Integer> tagStore = new HashMap<>();
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

            //同步到es的语法
            TorrentRecord torrentRecord = new TorrentRecord();

            //标题
            torrentRecord.setTitle(item.getName());

            //索引
            torrentRecord.setIndexed(indexHandle(infos));
            torrentRecord.setUrl(String.format("magnet:?xt=urn:btih:%s", item.getHash()));

            //标签
            torrentRecord.setTags(tags);

            //采集时间
            torrentRecord.setCreateTime(item.getCreateTime());
            torrentRecord.setUpdateTime(item.getUpdateTime());


            //文件类型
            Set<String> fileTypes = new HashSet<>();
            //文件总尺寸
            long fileCountSize = 0;
            for (FileModel file : item.getFiles()) {
                fileCountSize += file.getSize();
                fileTypes.add(FilenameUtils.getExtension(file.getPath()));
            }

            torrentRecord.setFileCount(item.getFiles().length);
            torrentRecord.setFileTypes(fileTypes);
            torrentRecord.setFileSize(fileCountSize);


            //内存记录当前标签使用次数
            incTags(tagStore, tags);
            torrentRecordSet.add(torrentRecord);
        }

        if (torrentRecordSet.size() > 0) {
            torrentRecordDao.saveAll(torrentRecordSet);
        }

        //记录标签到数据库
        recordTag(tagStore);


    }

    /**
     * 内存记录标签使用情况
     *
     * @param items
     * @param tags
     */
    private void incTags(Map<String, Integer> items, Set<String> tags) {
        for (String tag : tags) {
            Integer count = items.get(tag);
            if (count == null) {
                count = 0;
            }
            count++;
            items.put(tag, count);
        }
    }

    //记录多个被使用的标签
    private void recordTag(Map<String, Integer> tags) {
        this.tagRecordDao.inc(tags);
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
