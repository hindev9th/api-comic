package com.example.test.demo.Network;

import com.example.test.demo.Helpers.CommonHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CategoryNetwork {
    public static Elements getList(Document doc) throws IOException {
        Elements comicElements = (Elements) doc.select("#mainNav .dropdown-menu.megamenu .clearfix li");
        return comicElements;
    }

    public static String getName(Element element) {
        String name = element.select("a").attr("title");
        return name;
    }

    public static String getTitle(Element element) {
        String title = element.select("a").attr("data-title");
        return title;
    }

    public static String getUrl(Element element) {
        String url = element.select("a").attr("href");
        return url.replace(CommonHelper.getEnv("BASE_COMIC_URL"),"");
    }
}
