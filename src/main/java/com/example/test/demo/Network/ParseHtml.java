package com.example.test.demo.Network;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class ParseHtml {
    public static final String BASE_COMIC_URL = "https://www.nettruyentt.com/";
    public static final String BASE_IMAGE_URL = "//st.nettruyentt.com/";
    public static Document getHtml(String url) throws IOException {
        Connection.Response res = Jsoup.connect(url)
                .header("Content-Type","application/x-www-form-urlencoded")
                .cookie("TALanguage", "ALL")
                .ignoreHttpErrors(true)
                .sslSocketFactory(ParseHtml.socketFactory())
                .data("mode", "filterReviews")
                .data("filterRating", "")
                .data("filterSegment", "")
                .data("filterSeasons", "")
                .data("filterLang", "ALL")
                .header("X-Requested-With", "XMLHttpRequest")
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .method(Connection.Method.GET)
                .execute();
        return res.parse();
    }

    static private SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory result = sslContext.getSocketFactory();

            return result;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }
}
