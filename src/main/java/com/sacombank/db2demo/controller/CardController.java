package com.sacombank.db2demo.controller;

import com.google.gson.Gson;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import com.sacombank.db2demo.model.request.UserCardInfoRequest;
import com.sacombank.db2demo.service.CardInfoService;
import com.sacombank.db2demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/card-info/")
@RequiredArgsConstructor
public class CardController {

    private final MessageService messageService;
    private final ProducerTemplate producerTemplate;

    /**
        Store procedure
    */
    @PostMapping("message/addcarduser/sp")
    public ResponseEntity<?> springJpaAddCardByUserWithSP(@RequestBody UserCardInfoRequest request) {

        messageService.sendMessageToQueue("anhnh.spring.sp.addcarduser.request", request);

        return ResponseEntity.ok(true);
    }

    @PostMapping("message/addcarduser/sp/error")
    public ResponseEntity<?> springJpaAddCardByUserWithSPError(@RequestBody UserCardInfoRequest request) {

        messageService.sendMessageToQueue("anhnh.spring.sp.error.addcarduser.request", request);

        return ResponseEntity.ok(true);
    }

    @GetMapping("message/sp/{id}")
    public ResponseEntity<?> springJpaGetOneCardWithSP(@PathVariable Long id) {
        var response = producerTemplate.requestBody("direct:getcardbyidwithsp", id);
        return ResponseEntity.ok(response);
    }
}
