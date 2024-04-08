package com.example.test.demo.Network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ComicNetwork {
    public static Elements getList(Document doc) throws IOException {
        Elements comicElements = (Elements) doc.select("div.ModuleContent div.items div.row div.item");
        return comicElements;
    }

    public static String getId(Element element) {
        String id = element.select(".comic-item").attr("data-id");
        return id;
    }

    public static String getName(Element element) {
        String name = element.select("figcaption h3").text();
        return name;
    }

    public static String getImage(Element element) {
        String imageUrl = element.select(".image img").attr("data-original");
        return imageUrl.replace(ParseHtml.BASE_IMAGE_URL, "");
    }

    public static String getUrl(Element element) {
        String url = element.select(".image a").attr("href");
        return url.replace(ParseHtml.BASE_COMIC_URL, "");
    }

    public static String getDescription(Element element) {
        String description = element.select(".box_tootip .box_li .box_text").text();
        return description;
    }

    public static Elements getListInfo(Element element) {
        Elements select = element.select(".box_tootip .box_li .clearfix .message_main p");
        return select;
    }

    public static String getAnotherName(Element element) {
        Elements info = getListInfo(element);
        String anotherName = info.first().text();
        if (anotherName.contains("Tên khác:")){
            return anotherName.replace("Tên khác:", "");
        }
        return "";
    }

    public static String getCategories(Element element) {
        Elements info = getListInfo(element);
        String categories = info.first().text();
        if (!categories.contains("Thể loại:")){
            categories = info.get(1).text();
        }
        return categories.replace("Thể loại:", "");
    }

    public static String getStatus(Element element) {
        Elements info = getListInfo(element);
        String status = info.get(info.size() - 5).text();
        return status.replace("Tình trạng:", "");
    }

    public static String getViewCount(Element element) {
        Elements info = getListInfo(element);
        String view = info.get(info.size() - 4).text();
        return view.replace("Lượt xem:", "");
    }

    public static String getFollowCount(Element element) {
        Elements info = getListInfo(element);
        String follow = info.get(info.size() - 2).text();;
        return follow.replace("Theo dõi:", "");
    }
}
