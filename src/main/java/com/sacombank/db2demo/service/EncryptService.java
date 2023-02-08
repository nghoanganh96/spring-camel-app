package com.sacombank.db2demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
public class EncryptService {
    public String decode(String encodeString) {
        return new String(Base64.getDecoder().decode(encodeString));
    }
}
