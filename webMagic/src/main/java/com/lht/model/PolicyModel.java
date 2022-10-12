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
@TargetUrl("https://hqt.link2x.cn/hqt/policy/analysis.html")
public class PolicyModel {

    @ExtractBy(value = "//div[@id='AuthInfo']//li/a/text()")
    private String title;

    @ExtractBy(value = "//div[@id='AuthInfo']//li//span[1]/text()")
    private String time;

    //@ExtractBy(value = "//div[@id='AuthInfo']//li//span[4]/text()")
    //private String unit;

    public static void main(String[] args) {
        OOSpider.create(
                Site.me(),
                new ConsolePageModelPipeline(), PolicyModel.class).addUrl("https://hqt.link2x.cn/hqt/policy/analysis.html").run();
    }
}
