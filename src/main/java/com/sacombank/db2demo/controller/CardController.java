package com.sacombank.db2demo.controller;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.Constant;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import com.sacombank.db2demo.model.request.UserCardInfoRequest;
import com.sacombank.db2demo.service.CardInfoService;
import com.sacombank.db2demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/app/v1/card-info")
@RequiredArgsConstructor
public class CardController {

    private final MessageService messageService;
    private final ProducerTemplate producerTemplate;

    @PostMapping("/sendmsg")
    public ResponseEntity<?> springJpaAddCardByUserWithSP() {
        messageService.sendMessageToQueue(Constant.QUEUE_NAME_REQUEST, "current date time: " + LocalDateTime.now());
        return ResponseEntity.ok(true);
    }

    /**
        Store procedure
    */
    @PostMapping("/addcarduser")
    public ResponseEntity<?> springJpaAddCardByUserWithSP(@RequestBody UserCardInfoRequest request) {
        messageService.sendMessageToQueue("anhnh.spring.sp.addcarduser.request", request);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/addcarduser/error")
    public ResponseEntity<?> springJpaAddCardByUserWithSPError(@RequestBody UserCardInfoRequest request) {
        messageService.sendMessageToQueue("anhnh.spring.sp.error.addcarduser.request", request);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/deletecarduser/{id}")
    public ResponseEntity<?> springJpaDeleteCardUserWithSP(@PathVariable Long id) {
        messageService.sendMessageToQueue("anhnh.spring.sp.deletecarduser.request", id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/deletecarduser/error/{id}")
    public ResponseEntity<?> springJpaDeleteCardUserWithSPError(@PathVariable Long id) {
        messageService.sendMessageToQueue("anhnh.spring.sp.error.deletecarduser.request", id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> springJpaGetOneCardWithSP(@PathVariable Long id) {
        var response = producerTemplate.requestBody("direct:getcardbyidwithsp", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> springJpaGetAllCardWithSP() {
        var response = producerTemplate.requestBody("direct:getallcardwithsp", null, List.class);
        return ResponseEntity.ok(response);
    }
}
