package com.example.test.demo.Services;

import com.example.test.demo.Http.Responses.ApiResponse;
import com.example.test.demo.Http.Responses.ErrorResponse;
import com.example.test.demo.Http.Responses.SuccessResponse;
import com.example.test.demo.Network.PageImageNetwork;
import com.example.test.demo.Network.ParseHtml;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {
    public ApiResponse<?> getList(Optional<String> url){
        try {
            if (url.isPresent()){
                Document document = ParseHtml.getHtml(url.get());
                Map<String, Object> result = new HashMap<>();
                Elements elements = PageImageNetwork.getListPages(document);
                result.put("list",elements.stream().map(page -> {
                    Map<String,Object> itemMap = new HashMap<>();
                    String imageUrl = PageImageNetwork.getImageUrl(page);
                    itemMap.put("image_url",imageUrl);
                    return itemMap;
                }));
                return new SuccessResponse<>(result);
            }
        } catch (IOException e) {
            return new ErrorResponse<>();
        }
        return new ErrorResponse<>();
    }
}
