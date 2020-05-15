package com.lht.esapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author lhtao
 * @date 2020/5/15 14:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "lht_user")
public class User {

    private String name;

    private String code;

    private Integer age;
}
