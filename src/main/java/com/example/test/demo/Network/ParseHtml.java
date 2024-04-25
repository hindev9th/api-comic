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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public static void toDataUrl(String url, DataUrlCallback callback) {
        HttpURLConnection connection = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;

        try {
            URL imageUrl = new URL(url);
            connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();

            is = connection.getInputStream();
            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] imageData = baos.toByteArray();
            String base64Image = java.util.Base64.getEncoder().encodeToString(imageData);

            callback.onDataUrlGenerated("data:image/jpeg;base64," + base64Image);

        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());

        } finally {
            try {
                if (is != null) is.close();
                if (baos != null) baos.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}