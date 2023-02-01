package com.sacombank.db2demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectResponse {
    private String message;
    private int code;
    private Object data;

    public static ObjectResponse buildFailed(String message) {
        return new ObjectResponse("Process Failed: " + message, -1);
    }

    public static ObjectResponse buildSuccess(Object data) {
        return new ObjectResponse("Process Success.", 0, data);
    }

    public ObjectResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
