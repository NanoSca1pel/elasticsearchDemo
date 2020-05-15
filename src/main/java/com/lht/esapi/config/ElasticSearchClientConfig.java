package com.lht.esapi.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lhtao
 * @date 2020/5/15 10:38
 */
@Configuration
public class ElasticSearchClientConfig {

    //spring xml <beans id="restHighLevelClient" class="RestHighLevelClient">
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        //多集群环境可以配置多个地址，这里只配置一哥
                        new HttpHost("localhost", 9200, "http")));
                        //new HttpHost("localhost", 9201, "http")));
        return client;
    }
}
