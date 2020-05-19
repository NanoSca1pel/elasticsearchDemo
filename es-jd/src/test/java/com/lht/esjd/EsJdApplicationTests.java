package com.lht.esjd;

import com.lht.esjd.entity.JdContent;
import com.lht.esjd.utils.HtmlParseUtil;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EsJdApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate template;

    @Test
    void contextLoads() {
        template.indexOps(IndexCoordinates.of("jd_goods")).delete();

    }

    @Test
    public void test() throws IOException {
        List<JdContent> contents = HtmlParseUtil.parseJD("java");
        List<IndexQuery> queryList = new ArrayList<>(contents.size());
        for (JdContent content : contents) {
            IndexQuery query = new IndexQuery();
            query.setObject(content);
            queryList.add(query);
        }

        List<String> jd_goods = template.bulkIndex(queryList, BulkOptions.defaultOptions(), IndexCoordinates.of("jd_goods"));
        jd_goods.forEach(System.out::println);
    }

    @Test
    public void test2() throws IOException {
        PageRequest pageRequest = PageRequest.of(1, 10);

        NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(new TermQueryBuilder("title", "java")).withPageable(pageRequest).build();
        SearchHits<JdContent> hits = template.search(build, JdContent.class, IndexCoordinates.of("jd_goods"));
        hits.forEach(System.out::println);

    }
}
