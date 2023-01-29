package com.sacombank.db2demo.controller;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.Constant;
import com.sacombank.db2demo.model.request.CardInfoRequest;
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

    @PostMapping("/message")
    public ResponseEntity<?> message(@RequestBody CardInfoRequest request) {
        messageService.sendMessageToQueue(Constant.QUEUE_NAME_REQUEST, request);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/message/insert-sql")
    public ResponseEntity<?> messageInsertSql(@RequestBody CardInfoRequest request) {
        messageService.sendMessageToQueue(Constant.QUEUE_NAME_INSERT_SQL_REQUEST, request);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/message/get-all")
    public ResponseEntity<?> getAllCardInfo() {
        var response = producerTemplate.requestBody("direct:selectAll", null, List.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/message/get/{id}")
    public ResponseEntity<?> getCardInfo(@PathVariable Long id) {
        var response = producerTemplate.requestBody("direct:select", gson.toJson(id), List.class);

        return ResponseEntity.ok(response);
    }
}
