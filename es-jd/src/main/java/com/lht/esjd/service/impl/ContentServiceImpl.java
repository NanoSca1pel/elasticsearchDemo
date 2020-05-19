package com.lht.esjd.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lht.esjd.entity.JdContent;
import com.lht.esjd.service.ContentService;
import com.lht.esjd.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lhtao
 * @date 2020/5/18 13:28
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ElasticsearchRestTemplate template;

    @Override
    public Boolean parseContent(String keyword) throws Exception {
        List<JdContent> contents = HtmlParseUtil.parseJD(keyword);

        BulkRequest request = new BulkRequest("jd_goods");
        request.timeout("2m");

        for (JdContent content : contents) {
            request.add(new IndexRequest().source(JSONObject.toJSONString(content), XContentType.JSON));
        }

        BulkResponse bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);

        return !bulk.hasFailures();


        //TODO test
        /*List<JdContent> contents = HtmlParseUtil.parseJD(keyword);
        List<IndexQuery> queryList = new ArrayList<>(contents.size());
        for (JdContent content : contents) {
            IndexQuery query = new IndexQuery();
            query.setObject(content);
            queryList.add(query);
        }

        List<String> jd_goods = template.bulkIndex(queryList, BulkOptions.defaultOptions(), IndexCoordinates.of("jd_goods"));
        jd_goods.forEach(System.out::println);
        return true;*/
    }

    @Override
    public List<Map<String, Object>> searchPage(String keyword, Integer pageNo, Integer pageSize) throws IOException {
        List<Map<String, Object>> rtnList = new ArrayList<>();
        if (pageNo < 1) {
            pageNo = 1;
        }
        SearchRequest request = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));
        searchSourceBuilder.from((pageNo-1) * pageSize);
        searchSourceBuilder.size(pageSize);

        request.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        for (SearchHit searchHit : response.getHits().getHits()) {
            rtnList.add(searchHit.getSourceAsMap());
        }
        return rtnList;
    }

    @Override
    public List<Map<String, Object>> searchHighLightPage(String keyword, Integer pageNo, Integer pageSize) throws IOException {
        List<Map<String, Object>> rtnList = new ArrayList<>();
        if (pageNo < 1) {
            pageNo = 1;
        }
        SearchRequest request = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //精准匹配 不分词
        MatchQueryBuilder termQueryBuilder = new MatchQueryBuilder("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));

        //分页
        searchSourceBuilder.from((pageNo-1) * pageSize);
        searchSourceBuilder.size(pageSize);

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //多个高亮显示关闭（只显示第一个） 使用时好像有问题
        //highlightBuilder.requireFieldMatch(false);
        highlightBuilder.field("title");
        //设置高亮标签 前缀
        highlightBuilder.preTags("<span style='color:red'>");
        //设置高亮标签 后缀
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);


        //执行搜索
        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        //结果解析
        for (SearchHit searchHit : response.getHits().getHits()) {

            //解析高亮字段
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

            //高亮字段置换
            if (title != null) {
                Text[] fragments = title.getFragments();
                String newTitle = "";
                for (Text text : fragments) {
                    newTitle += text;
                }
                sourceAsMap.put("title", newTitle);
            }
            rtnList.add(sourceAsMap);
        }
        return rtnList;
    }
}
