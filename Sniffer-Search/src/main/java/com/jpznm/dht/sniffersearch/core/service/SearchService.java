package com.jpznm.dht.sniffersearch.core.service;

import com.fast.dev.data.base.util.PageEntityUtil;
import com.jpznm.dht.snifferdao.dao.HotWordsDao;
import com.jpznm.dht.snifferdao.dao.TagRecordDao;
import com.jpznm.dht.snifferdao.domain.HotWords;
import com.jpznm.dht.sniffersearch.core.conf.ZNMConfig;
import com.jpznm.dht.sniffersearch.core.dao.TorrentRecordDao;
import com.jpznm.dht.sniffersearch.core.domain.TorrentRecord;
import com.jpznm.dht.sniffersearch.core.model.HotWordsModel;
import com.jpznm.dht.sniffersearch.core.model.TagViewModel;
import com.jpznm.dht.sniffersearch.core.model.TorrentRecordModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class SearchService {


    @Autowired
    private ZNMConfig znmConfig;


    @Autowired
    private TorrentRecordDao torrentRecordDao;


    @Autowired
    private TagRecordDao tagRecordDao;

    @Autowired
    private HotWordsDao hotWordsDao;


    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 分页查询
     *
     * @param keyWord
     * @param pageable
     * @return
     */
    public Page<TorrentRecordModel> query(String keyWord, Pageable pageable) {

        addHotWords(keyWord);


        return PageEntityUtil.concurrent2PageModel(this.torrentRecordDao.query(keyWord, pageable), new PageEntityUtil.DataClean<TorrentRecord, TorrentRecordModel>() {
            @Override
            public TorrentRecordModel execute(TorrentRecord data) {
                TorrentRecordModel torrentRecordModel = new TorrentRecordModel();
                BeanUtils.copyProperties(data, torrentRecordModel, "publishTime", "fileTotalSize");

                //发布时间
                torrentRecordModel.setPublishTime(simpleDateFormat.format(new Date(data.getCreateTime())));
                setFileTotalSize(torrentRecordModel, data.getFileSize());

                return torrentRecordModel;
            }
        });
    }

    /**
     * 记录热词
     */
    private void addHotWords(String keyWord) {
        try {
            hotWordsDao.addHotWords(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //格式化文件的尺寸
    private void setFileTotalSize(TorrentRecordModel torrentRecordModel, double fileSize) {
        String ret = null;
        if (fileSize < 1024) {
            ret = fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            ret = new BigDecimal(fileSize / 1024).setScale(2, RoundingMode.HALF_UP).doubleValue() + " K";
        } else if (fileSize < 1024 * 1024 * 1024) {
            ret = new BigDecimal(fileSize / 1024 / 1024).setScale(2, RoundingMode.HALF_UP).doubleValue() + " M";
        } else {
            ret = new BigDecimal(fileSize / 1024 / 1024 / 1024).setScale(2, RoundingMode.HALF_UP).doubleValue() + " G";
        }
        torrentRecordModel.setFileTotalSize(ret);
    }


    /**
     * 获取热词列表
     *
     * @param pageable
     * @return
     */
    public Page<HotWordsModel> getHotWords(Pageable pageable) {
        return PageEntityUtil.concurrent2PageModel(this.hotWordsDao.listHotWords(pageable), new PageEntityUtil.DataClean<HotWords, HotWordsModel>() {
            @Override
            public HotWordsModel execute(HotWords data) {
                HotWordsModel hotWordsModel = new HotWordsModel();
                BeanUtils.copyProperties(data, hotWordsModel);
                return hotWordsModel;
            }
        });
    }


    /**
     * 获取标签的视图
     *
     * @return
     */
    public TagViewModel[] getTags() {
        return this.tagRecordDao
                .findByNameIn(znmConfig.getTags()
                        .stream()
                        .map((it) -> {
                            return it.getName();
                        })
                        .collect(Collectors.toList()))
                .stream()
                .map((it) -> {
                    TagViewModel tagViewModel = new TagViewModel();
                    BeanUtils.copyProperties(it, tagViewModel);
                    return tagViewModel;
                }).toArray(TagViewModel[]::new);

    }


}
