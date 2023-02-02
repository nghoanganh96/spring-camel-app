package com.sacombank.db2demo.controller;

import com.google.gson.Gson;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import com.sacombank.db2demo.model.request.UserRequest;
import com.sacombank.db2demo.service.CardInfoService;
import com.sacombank.db2demo.service.MessageService;
import com.sacombank.db2demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageService messageService;
    private final Gson gson;
    private final ProducerTemplate producerTemplate;
    
    // Card Information
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getOneUser(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.save(request));
    }

    @PostMapping("/save/proc")
    public ResponseEntity<?> saveStoreProc(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.saveWithStoreProc(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.delete(id));
    }

}
