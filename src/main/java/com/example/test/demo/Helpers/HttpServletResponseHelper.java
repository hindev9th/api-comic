package com.example.test.demo.Helpers;

import com.example.test.demo.Enum.ApiStatus;
import com.example.test.demo.Http.Responses.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HttpServletResponseHelper {

    private final HttpServletResponse httpServletResponse;
    private ApiResponse<?> apiResponse;

    public HttpServletResponseHelper(HttpServletResponse response){
        this.httpServletResponse =response;
    }
    public HttpServletResponseHelper setApiResponse(ApiResponse<?> apiResponse){
        this.apiResponse =apiResponse;
        return this;
    }

    public void toResponse() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        this.httpServletResponse.setCharacterEncoding("UTF-8");
        this.httpServletResponse.setContentType("application/json;charset=UTF-8");
        this.httpServletResponse.setStatus(ApiStatus.OK);
        this.httpServletResponse.getWriter().write(objectMapper.writeValueAsString(this.apiResponse.toBody()));
    }
}
