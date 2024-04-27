package com.example.test.demo.Helpers;

import io.github.cdimascio.dotenv.Dotenv;

public class CommonHelper {
    public static String BASE_COMIC_URL;
    public static String BASE_IMAGE_URL;
    static Dotenv dotenv = Dotenv.load();
    public static String getEnv(String var) {
        String databaseUrl = dotenv.get(var);
        return databaseUrl == null ? "" : databaseUrl;
    }
}
