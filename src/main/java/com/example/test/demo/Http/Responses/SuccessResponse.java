package com.example.test.demo.Http.Responses;

import com.example.test.demo.Enum.ApiStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class SuccessResponse<T> extends ApiResponse{
    public SuccessResponse(Integer status, String message, Object data) {
        super(status, message, HttpStatus.OK, data);
    }

    public SuccessResponse(T data){
        super(ApiStatus.OK,"success",HttpStatus.OK,data);
    }

    public SuccessResponse(){
        super(ApiStatus.OK,"success",HttpStatus.OK,null);
    }
}
