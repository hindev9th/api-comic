package com.example.test.demo.Services;

import com.example.test.demo.Helpers.CommonHelper;
import com.example.test.demo.Http.Responses.ApiResponse;
import com.example.test.demo.Http.Responses.ErrorResponse;
import com.example.test.demo.Http.Responses.SuccessResponse;
import com.example.test.demo.Network.ChapterNetwork;
import com.example.test.demo.Network.ParseHtml;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChapterService {

    public ApiResponse<?> list(String urlKey){
        Map<String, Object> result = new HashMap<>();
        try {
            Document document  = ParseHtml.getHtml(ParseHtml.BASE_CRAWL_URL +  urlKey);
            Elements elements = ChapterNetwork.getList(document);
            result.put("list",elements.stream().map(item -> {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("id",ChapterNetwork.getId(item));
                itemMap.put("name",ChapterNetwork.getName(item));
                itemMap.put("url",ChapterNetwork.getUrl(item));
                itemMap.put("time",ChapterNetwork.getTime(item));
                return itemMap;
            }));
        }catch (IOException e){
            return new ErrorResponse<>("Can not load chapter!");
        }
        return new SuccessResponse<>(result);
    }
}
