package com.uwaterloo.uwbubbles.controllers;

import com.uwaterloo.uwbubbles.dao.Message;
import com.uwaterloo.uwbubbles.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/message/publish")
    public ResponseEntity<String> sendMessage(@RequestBody Message msg) {
        messageService.saveMessage(msg);
        return new ResponseEntity<>("Message sent", HttpStatus.OK);
    }

    @GetMapping("/message/find/{id}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable long id) {
        List<Message> messages = messageService.findMessagesFor(id);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

}
