package com.example.test.demo.Network;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ChapterNetwork {
    public static String getId(Element element){
        String id = element.select("figcaption .comic-item .clearfix").first().select(" a").attr("data-id");
        return id;
    }

    public static String getName(Element element){
        String name = element.select("figcaption .comic-item .clearfix").first().select("a").text();
        return name;
    }

    public static String getUrl(Element element){
        String url = element.select("figcaption .comic-item .clearfix").first().select("a").attr("href");
        return url.replace(ParseHtml.BASE_COMIC_URL,"");
    }

    public static String getTime(Element element){
        String time = element.select("figcaption .comic-item .clearfix").first().select(".time").text();
        return time;
    }
}
