package com.lht.esjd.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lht.esjd.entity.JdContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lhtao
 * @date 2020/5/18 10:02
 */
public class HtmlParseUtil {

    public static void main(String[] args) throws Exception {
        List<JdContent> java = parseJD("心理学");
        java.forEach(System.out::println);
        System.out.println("============");

    }

    /*public static <T> List<T> parseJD(String keywords) throws IOException {
        //获取请求 https://search.jd.com/Search?keyword=java
        //前提，需要联网！ 无法获取ajax数据，如果想要获取，需要模拟客户端（浏览器）请求

        String url = "https://search.jd.com/Search?keyword="+keywords;

        //解析网页（Jsoup返回Document就是浏览器Document对象）
        Document document = Jsoup.parse(new URL(url), 30000);

        //所有在js中可以使用的方法，这里都能用
        Element element = document.getElementById("J_goodsList");

        //获取所有的li元素
        Elements elements = element.getElementsByTag("li");

        List<JdContent> list = new ArrayList<>();
        //获取元素中的内容，这里el就是每一个li标签
        for (Element el : elements) {
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            String img = el.getElementsByTag("img").eq(0).attr("src");
            // 网页图片可能会有懒加载的过程 source-data-lazy-img
            //String img = el.getElementsByTag("img").eq(0).attr("source-data-lazy-img");


            JdContent content = new JdContent();
            content.setTitle(title).setPrice(price).setImage(img);
            list.add(content);
        }
        return JSON.parseObject(JSONObject.toJSONString(list), new TypeReference<List<T>>(){});
    }*/

    public static <T> List<T> parseJD(String keywords) throws IOException {
        //获取请求 https://search.jd.com/Search?keyword=java
        //前提，需要联网！ 无法获取ajax数据，如果想要获取，需要模拟客户端（浏览器）请求

        String url = "https://search.jd.com/Search?keyword="+keywords;

        //解析网页（Jsoup返回Document就是浏览器Document对象）
        Document document = Jsoup.parse(new URL(url), 30000);

        //所有在js中可以使用的方法，这里都能用
        Element element = document.getElementById("J_goodsList");

        //获取所有的li元素
        Elements elements = element.getElementsByTag("li");

        List<T> list = new ArrayList<>();
        //获取元素中的内容，这里el就是每一个li标签
        for (Element el : elements) {
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            String img = el.getElementsByTag("img").eq(0).attr("src");
            // 网页图片可能会有懒加载的过程 source-data-lazy-img
            //String img = el.getElementsByTag("img").eq(0).attr("source-data-lazy-img");


            JdContent content = new JdContent();
            content.setTitle(title).setPrice(price).setImage(img);
            list.add((T)content);
        }
        return list;
    }
}
