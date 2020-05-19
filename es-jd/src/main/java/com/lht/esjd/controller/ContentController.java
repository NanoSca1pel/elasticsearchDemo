package com.lht.esjd.controller;

import com.lht.esjd.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author lhtao
 * @date 2020/5/18 13:29
 */
@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) throws Exception {
        Boolean flag = contentService.parseContent(keyword);
        return flag;
    }

    /*@GetMapping("/search/{keyword}/{pageNum}/{pageSize}")
    public List<Map<String, Object>> searchPage(@PathVariable("keyword") String keyword, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) throws Exception {
        return contentService.searchPage(keyword, pageNum, pageSize);
    }*/

    @GetMapping("/search/{keyword}/{pageNum}/{pageSize}")
    public List<Map<String, Object>> searchPage(@PathVariable("keyword") String keyword, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) throws Exception {
        return contentService.searchHighLightPage(keyword, pageNum, pageSize);
    }
}
