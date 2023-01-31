package com.sacombank.db2demo.model;

import lombok.Data;

@Data
public class MessageResponse {
    private String message;
    private int code;
    private Object data;
}
