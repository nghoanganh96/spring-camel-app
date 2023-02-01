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

import java.util.List;

@RestController
@RequestMapping("/app/v1/card-info/")
@RequiredArgsConstructor
public class CardController {

    private final CardInfoService cardInfoService;
    private final MessageService messageService;
    private final Gson gson;
    private final ProducerTemplate producerTemplate;

    // Card Information
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cardInfoService.getById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CardInfoRequest request) {
        return ResponseEntity.ok(cardInfoService.save(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(cardInfoService.delete(id));
    }

    @PostMapping("/message/save")
    public ResponseEntity<?> springJpaSave(@RequestBody CardInfoRequest request) {
        messageService.sendMessageToQueue("anhnh.spring.save.request", request);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/message/{id}")
    public ResponseEntity<?> springJpaGetOne(@PathVariable Long id) {
        messageService.sendMessageToQueue("anhnh.spring.getone.request", id);

        return ResponseEntity.ok(true);
    }

    @PostMapping("message/addcardbyuser")
    public ResponseEntity<?> springJpaAddCardByUser(@RequestBody UserCardInfoRequest request) {

        messageService.sendMessageToQueue("anhnh.spring.addcardbyuser.request", request);

        return ResponseEntity.ok(true);
    }
}
