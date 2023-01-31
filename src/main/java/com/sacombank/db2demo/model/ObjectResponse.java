package com.sacombank.db2demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectResponse {
    private String message;
    private String code;
    private Object data;

    public ObjectResponse(String message, String code) {
        this.message = message;
        this.code = code;
        this.data = null;
    }
}
