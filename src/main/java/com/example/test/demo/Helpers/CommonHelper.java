package com.example.test.demo.Helpers;

import io.github.cdimascio.dotenv.Dotenv;

public class CommonHelper {
    public static String getEnv(String var) {
        Dotenv dotenv = Dotenv.load(); // Load .env file
        String databaseUrl = dotenv.get(var);
        return databaseUrl == null ? "" : databaseUrl;
    }
}
