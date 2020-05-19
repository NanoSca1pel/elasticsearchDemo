package com.lht.esjd.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lhtao
 * @date 2020/5/18 10:51
 */
@Data
@Accessors(chain = true)
public class JdContent {

    private String title;

    private String price;

    private String image;
}
