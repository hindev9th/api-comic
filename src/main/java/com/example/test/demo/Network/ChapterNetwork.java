package com.example.test.demo.Network;

import com.example.test.demo.Helpers.CommonHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ChapterNetwork {

    public static Elements getList(Document doc) throws IOException {
        Elements comicElements = (Elements) doc.select("#nt_listchapter nav li");
        return comicElements;
    }
    public static String getId(Element element){
        String id = element.select(".chapter a").attr("data-id");
        return id;
    }

    public static String getName(Element element){
        String name = element.select(".chapter a").text();
        return name;
    }

    public static String getUrl(Element element){
        String url = element.select(".chapter a").attr("href");
        return url;
    }

    public static String getTime(Element element){
        String time = element.select(".col-xs-4.text-center.no-wrap.small").text();
        return time;
    }
}
