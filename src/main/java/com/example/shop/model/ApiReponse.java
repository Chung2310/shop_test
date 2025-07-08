package com.example.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiReponse<T> {
    private int status;
    private String message;
    private T result;
}
