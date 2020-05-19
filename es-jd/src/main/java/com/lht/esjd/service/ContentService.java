package com.lht.esjd.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author lhtao
 * @date 2020/5/18 13:27
 */
public interface ContentService {

    Boolean parseContent(String keyword) throws Exception;

    List<Map<String, Object>> searchPage(String keyword, Integer pageNo, Integer pageSize) throws IOException;

    List<Map<String, Object>> searchHighLightPage(String keyword, Integer pageNo, Integer pageSize) throws IOException;

}
