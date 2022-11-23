package com.uwaterloo.uwbubbles.controllers;

import com.uwaterloo.uwbubbles.dao.Message;
import com.uwaterloo.uwbubbles.dao.User;
import com.uwaterloo.uwbubbles.services.MessageService;
import com.uwaterloo.uwbubbles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/message/publish")
    public ResponseEntity<String> sendMessage(HttpServletRequest request, @RequestBody Message msg) {
        User user = userService.getUserFromJwt(request);
        long senderID = user.getId();
        msg.setSender(senderID);
        messageService.saveMessage(msg);
        return new ResponseEntity<>("Message sent", HttpStatus.OK);
    }

    @GetMapping("/message/find")
    public ResponseEntity<List<Message>> getMessages(HttpServletRequest request, @RequestParam long pairId) {
        User user = userService.getUserFromJwt(request);
        List<Message> messages = messageService.findMessagesBetween(user.getId(), pairId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

}
