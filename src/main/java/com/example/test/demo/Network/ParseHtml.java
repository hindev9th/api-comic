package com.example.test.demo.Network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ParseHtml {
    public static final String BASE_COMIC_URL = "https://www.nettruyentt.com/";
    public static final String BASE_IMAGE_URL = "//st.nettruyentt.com/";
    public static Document getHtml(String url) throws IOException {
        Document doc = (Document) Jsoup.connect(url)
                .userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)")
                .timeout(5000) // Set timeout in milliseconds
                .followRedirects(true)
                .get();
        return doc;
    }
}
