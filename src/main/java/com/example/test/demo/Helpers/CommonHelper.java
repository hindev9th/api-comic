package com.example.test.demo.Helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.Map;

public class CommonHelper {
    public static final String BASE_COMIC_URL = "https://www.nettruyenvv.com/";
    public static final String BASE_IMAGE_URL = "//st.nettruyenvv.com/";

    public static String getEnv(String var) {

        String envData = Dotenv.load().get(var);;
        return envData == null ? "" : envData;
    }

    public static Map<String, Object> stringToJsonMap(String data) {
        try {
            ObjectMapper resMapper = new ObjectMapper();
            return resMapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
