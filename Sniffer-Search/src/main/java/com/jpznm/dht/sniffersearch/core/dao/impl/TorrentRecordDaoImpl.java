package com.jpznm.dht.sniffersearch.core.dao.impl;

import com.jpznm.dht.sniffersearch.core.dao.extend.TorrentRecordDaoExtend;
import com.jpznm.dht.sniffersearch.core.domain.TorrentRecord;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TorrentRecordDaoImpl implements TorrentRecordDaoExtend {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private final static String[] FindField = new String[]{"title", "indexed"};
    //标签的名称
    private final static String TagsName = "tag:";


    @Override
    public Page<TorrentRecord> query(String keyWord, String preTag, String postTag, Pageable pageable) {


        //关键词为空字符串
        QueryBuilder queryBuilder = null;
        if (!StringUtils.isEmpty(keyWord)) {
            queryBuilder = createQueryBuilder(keyWord);
        }

        //查询条件与分页
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable);


        //查询总记录数
        long total = this.elasticsearchTemplate.count(nativeSearchQueryBuilder.build(), TorrentRecord.class);


        //高亮
        if (!StringUtils.isEmpty(preTag) && !StringUtils.isEmpty(postTag)) {
            List<HighlightBuilder.Field> fields = new ArrayList<>();
            for (String s : FindField) {
                fields.add((new HighlightBuilder.Field("ideaTitle").preTags(preTag).postTags(postTag)));
            }
            nativeSearchQueryBuilder.withHighlightFields(fields.toArray(new HighlightBuilder.Field[0]));
        }


        return elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), TorrentRecord.class);
    }

    @Override
    public Page<TorrentRecord> query(String keyWord, Pageable pageable) {
        //关键词为空字符串
        QueryBuilder queryBuilder = null;
        if (!StringUtils.isEmpty(keyWord)) {
            queryBuilder = createQueryBuilder(keyWord);
        }

        //查询条件与分页
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable);

        return elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), TorrentRecord.class);
    }


    /**
     * 构建查询条件
     *
     * @param keyWord
     * @return
     */
    private static QueryBuilder createQueryBuilder(final String keyWord) {
        if (keyWord.length() > 4 && keyWord.substring(0, 4).equalsIgnoreCase(TagsName)) {
            return QueryBuilders.matchPhraseQuery("tags", keyWord.substring(TagsName.length() , keyWord.length()));
        }


        // 组合查询
//        BoolQueryBuilder rootQueryBuilder = QueryBuilders.boolQuery();

        // 查询 title and
//        BoolQueryBuilder titleQueryBuilder = QueryBuilders.boolQuery();
//        titleQueryBuilder.must(QueryBuilders.matchPhraseQuery("title", keyWord));
        // for (String kw : StringSplit.split(wd)) {
        // titleQueryBuilder.must(QueryBuilders.matchPhraseQuery(TitleName, kw));
        // }

        // 查询 index or
//        BoolQueryBuilder indexQueryBuilder = QueryBuilders.boolQuery();
//        indexQueryBuilder.should(QueryBuilders.matchPhraseQuery("indexed", keyWord));

        // (title1 and title2) or (index1 or index2)
//        return rootQueryBuilder.should(titleQueryBuilder).should(indexQueryBuilder);

//        for (String field : FindField) {
//            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//            QueryBuilders.matchPhraseQuery(field, keyWord);
//
//            // or
//            rootQueryBuilder.should(queryBuilder);
//        }
//        return rootQueryBuilder;


        return QueryBuilders.matchPhraseQuery("indexed", keyWord);
    }
}
