package com.lht.model;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * @author lhtao
 * @descript
 * @date 2021/5/13 16:18
 */
@TargetUrl("http://hqt.link2x.cn/hqt/policyCenter/policyConsult.html")
public class PolicyModel {

    @ExtractBy(value = "//div[@class='list fl']//li/a/text()")
    private String title;

    @ExtractBy(value = "//div[@class='list fl']//li//span[1]/text()")
    private String time;

    @ExtractBy(value = "//div[@class='list fl']//li//span[4]/text()")
    private String unit;

    public static void main(String[] args) {
        OOSpider.create(
                Site.me(),
                new ConsolePageModelPipeline(), PolicyModel.class).addUrl("http://hqt.link2x.cn/hqt/policyCenter/policyConsult.html").run();
    }
}
