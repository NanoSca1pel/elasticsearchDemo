package com.lht.model;

import org.apache.commons.collections.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.List;

/**
 * @author lhtao
 * @descript
 * @date 2021/5/14 13:27
 */
public class PolicyPageProcessor implements PageProcessor {

    private Site site = Site.me();

    private static final String LIST_URL = "http://hqt.link2x.cn/api/api-policy/zcPolicy/list/.*";

    @Override
    public void process(Page page) {

        String s = new JsonPathSelector("$").select(page.getRawText());
        List<String> ids = new JsonPathSelector("$.obj.content[*].base_id").selectList(page.getRawText());
        if (CollectionUtils.isNotEmpty(ids)) {
            for (String id : ids) {
                page.addTargetRequest("http://hqt.link2x.cn/api/api-policy/zcPolicy/queryDetailById/" + id);
            }
        } else {
            page.putField("title", new JsonPathSelector("$.obj.title").select(page.getRawText()));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Request request = new Request();
        request.setMethod("POST");
        request.setUrl("http://hqt.link2x.cn/api/api-policy/zcPolicy/list/0/12");
        request.setRequestBody(HttpRequestBody.json("{\"policyInformation\":\"\",\"province\":\"CBA08CFF-F58C-464D-AF41-6D3682C3DE36\",\"city\":\"04FA5137-4D7C-4FD5-B6D7-DFF4AA583765\",\"sortId\":\"0\",\"sortType\":\"desc\"}\n", "UTF-8"));
        Spider.create(new PolicyPageProcessor()).addRequest(request).run();
    }
}