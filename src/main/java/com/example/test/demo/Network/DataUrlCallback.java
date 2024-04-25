package com.example.test.demo.Network;

public interface DataUrlCallback {
    void onDataUrlGenerated(String dataUrl);
    void onError(String errorMessage);
}
