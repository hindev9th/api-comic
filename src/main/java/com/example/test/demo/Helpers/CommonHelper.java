package com.example.test.demo.Helpers;

import io.github.cdimascio.dotenv.Dotenv;

public class CommonHelper {
    public static final String BASE_COMIC_URL = "https://www.nettruyenvv.com/";
    public static final String BASE_IMAGE_URL = "//st.nettruyenvv.com/";
    static Dotenv dotenv = Dotenv.load();
    public static String getEnv(String var) {
        String databaseUrl = dotenv.get(var);
        return databaseUrl == null ? "" : databaseUrl;
    }
}
