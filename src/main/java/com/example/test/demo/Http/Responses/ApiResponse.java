package com.example.test.demo.Http.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private final Integer status;
    private final String message;
    private final HttpStatus httpStatus;
    private final T data;

    public Map<String, Object> toBody(){
        Map<String, Object> body = new HashMap<>();

        body.put("data",this.data == null ? new HashMap<>() : this.data);
        body.put("message",this.message);
        body.put("status",this.status);
        return body;
    }

    public ResponseEntity<Map<String,Object>> responseEntity(){
        return ResponseEntity.status(HttpStatus.OK).body(this.toBody());
    }
}
