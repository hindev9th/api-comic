package com.example.test.demo.Network;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PageImageNetwork {
    public static Elements getListPages(Document document){
        Elements elements = document.select("#ctl00_divCenter .page-chapter");
        return elements;
    }

    public static String getImageUrl(Element element){
        String url = element.select("img").attr("data-src");
        return url;
    }
}
