package com.lht.esapi;

import com.alibaba.fastjson.JSON;
import com.lht.esapi.entity.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class EsApiApplicationTests {

    //@Resource(name = "restHighLevelClient")

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Autowired
    private ElasticsearchRestTemplate template;

    //创建索引
    @Test
    void createIndex() throws IOException {
        //① 创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("test_index");
        //复制代码
        Map<String, Object> message = new HashMap<>();
        message.put("type", "text");
        Map<String, Object> properties = new HashMap<>();
        properties.put("message", message);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        request.mapping(mapping);
        //② 客户端执行请求 IndicesClient, 请求后获得响应
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    //创建索引
    @Test
    void createIndex2() throws IOException {
        boolean response2 = template.createIndex("test_index2");
        System.out.println(response2);
    }

    //判断索引是否存在
    @Test
    void existIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("test_index");
        boolean response = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    //判断索引是否存在
    @Test
    void existIndex2() throws IOException {
        boolean response = template.indexExists("test_index2");
        System.out.println(response);
    }

    //获取索引信息
    @Test
    void getIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("test_index");
        GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    //删除索引信息
    @Test
    void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("test_index");
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    //删除索引信息
    @Test
    void deleteIndex2() throws IOException {
        boolean response = template.deleteIndex("test_index2");
        System.out.println(response);
    }

    //添加文档
    @Test
    void addDocument() throws IOException {
        //创建实体对象
        User user = new User("王文源", "1", 27);

        //创建请求
        IndexRequest request = new IndexRequest("lht_user");

        //规则 PUT /lht_user/_doc/1
        request.id("1");
        request.timeout("1s");
        //request.timeout(TimeValue.timeValueSeconds(1));

        //将我们的数据放入请求 json
        request.source(JSON.toJSONString(user), XContentType.JSON);

        //客户端发送请求，获取响应结果
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        System.out.println(response.toString());
        System.out.println(response.status()); //对应命令返回的状态

    }

    //添加文档
    @Test
    void addDocument2() throws IOException {
        //创建实体对象
        User user = new User("王文源", "1", 27);

        //创建请求
        IndexRequest request = new IndexRequest("lht_user");

        //规则 PUT /lht_user/_doc/1
        request.id("1");
        request.timeout("1s");
        //request.timeout(TimeValue.timeValueSeconds(1));

        //将我们的数据放入请求 json
        request.source(JSON.toJSONString(user), XContentType.JSON);

        IndexResponse response = template.getClient().index(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
        System.out.println(response.status()); //对应命令返回的状态

    }

    //判断文档信息是否存在
    @Test
    void existDocument() throws IOException {
        GetRequest request = new GetRequest("lht_user", "1");
        //不获取返回的_source的上下文, 可以提高效率
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");

        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //查询文档信息
    @Test
    void getDocument() throws IOException {
        GetRequest request = new GetRequest("lht_user", "1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());//打印文档的内容
    }

    //更新文档信息
    @Test
    void updateDocument() throws IOException {
        User user = new User("王文源", "1", 27);

        UpdateRequest request = new UpdateRequest("lht_user","1");
        request.timeout("1s");

        request.doc(JSON.toJSONString(user), XContentType.JSON);

        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

        System.out.println(response.toString());
        System.out.println(response.status());

    }

    //删除文档信息
    @Test
    void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("lht_user","1");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

        System.out.println(response.toString());
        System.out.println(response.status());

    }

    //批量插入
    @Test
    void batchAddDocuments() throws IOException {
        List<User> list = new ArrayList<>();
        list.add(new User("王文源1","1", 21));
        list.add(new User("王文源2","2", 22));
        list.add(new User("王文源3","3", 23));
        list.add(new User("王文源4","4", 24));
        list.add(new User("王文源5","5", 25));
        list.add(new User("王文源6","6", 26));
        list.add(new User("王文源7","7", 27));
        list.add(new User("王文源8","8", 28));

        BulkRequest request = new BulkRequest("lht_user");
        request.timeout("10s");

        //批处理请求
        for (int i=0; i<list.size(); i++) {
            //批量更新和批量删除只要在这里修改对应的请求就可以了
            request.add(new IndexRequest().id(i + 1 +"").source(JSON.toJSONString(list.get(i)), XContentType.JSON));
        }

        BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println(responses.hasFailures()); //判断是否存在失败 false就代表没有失败 即成功
    }

    //查询

    /**
     * SearchRequest 搜索请求
     * SearchSourceBuilder 条件构造
     * HighlightBuilder 构造高亮
     * TermQueryBuilder 精确查询 级别较低 这种查询适合keyword、numeric、date等明确值的
     * MatchQueryBuilder 分词查询
     * MatchPhraseQueryBuilder 全词查询
     * @throws IOException
     */
    @Test
    void searchDocument() throws IOException {
        SearchRequest request = new SearchRequest("lht_user");

        //构造搜索条件
        SearchSourceBuilder builder = new SearchSourceBuilder();

        //查询条件，我们可以使用QueryBuilders工具来实现
        //QueryBuilders.termQuery 精确
        //QueryBuiders.matchAllQuery() 匹配所有
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name.keyword", "王文源");
        //MatchQueryBuilder matchAllQueryBuilder = QueryBuilders.matchQuery("name", "王文源");

        builder.query(termQueryBuilder);
        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response.getHits()));
        Arrays.asList(response.getHits().getHits()).forEach(p -> System.out.println(p.getSourceAsMap()));
    }

    //查询
    @Test
    void searchDocument2() throws IOException {

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("name.keyword", "王文源")).build();
        List<User> users = template.queryForList(searchQuery, User.class);
        users.forEach(p-> System.out.println(p.toString()));
    }
}
