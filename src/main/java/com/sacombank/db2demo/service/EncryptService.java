package com.sacombank.db2demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;

@Service
@Slf4j
public class EncryptService {
    public String decode(String encodeString) {
        log.info("Encode String: {}", encodeString);
        var decodeString  = new String(Base64.getDecoder().decode(encodeString));
        log.info("Decode String: {}", decodeString);
        return decodeString;
    }
}
