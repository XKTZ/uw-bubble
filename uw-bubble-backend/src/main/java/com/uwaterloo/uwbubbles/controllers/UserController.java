package com.uwaterloo.uwbubbles.controllers;

import com.uwaterloo.uwbubbles.dao.Message;
import com.uwaterloo.uwbubbles.dao.User;
import com.uwaterloo.uwbubbles.dto.RecommendationResponse;
import com.uwaterloo.uwbubbles.repositories.MessageRepository;
import com.uwaterloo.uwbubbles.repositories.UserRepository;
import com.uwaterloo.uwbubbles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/users/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userService.credentialsExist(user)) {
            if (!userService.enabled(user.getUsername())) {
                String status = userService.sendVerificationEmail(user);
                if (status.equals("Unknown Error")) {
                    return new ResponseEntity<>("Unknown error, try again later", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                else {
                    return new ResponseEntity<>("A verification link has been resent", HttpStatus.OK);
                }
            }
            else {
                return new ResponseEntity<>("Username and Email already been used", HttpStatus.BAD_REQUEST);
            }
        }
        else if (userService.usernameExist(user.getUsername())) {
            return new ResponseEntity<>("Username is already been used", HttpStatus.BAD_REQUEST);
        }
        else if (userService.emailExist(user.getEmail())) {
            return new ResponseEntity<>("Email is already been used", HttpStatus.BAD_REQUEST);
        }
        else if (!user.getEmail().endsWith("@uwaterloo.ca")) {
            return new ResponseEntity<>("Invalid Email Address", HttpStatus.BAD_REQUEST);
        }
        else {
            user.setEnabled(false);
            userRepository.save(user);
            String status = userService.sendVerificationEmail(user);
            if (status.equals("Unknown Error")) {
                return new ResponseEntity<>("Unknown error, try again later", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            else {
                return new ResponseEntity<>("Account created! Please check your email for a verification link", HttpStatus.OK);
            }
        }
    }

    @GetMapping("/users/recommend")
    public ResponseEntity<List<RecommendationResponse>> userRecommendation(HttpServletRequest request) {
        User user = userService.getUserFromJwt(request);
        Map<Long, Double> recommendVal = userService.getRecommendedUsers(user);
        List<RecommendationResponse> res = new ArrayList<>();
        for (long id : recommendVal.keySet()) {
            if (userRepository.findUserById(id)==null) continue;
            res.add(new RecommendationResponse(userRepository.getById(id), recommendVal.get(id)));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/users/communications")
    public ResponseEntity<List<User>> userCommunication(HttpServletRequest request) {
        User user = userService.getUserFromJwt(request);
        var usrs = Stream.concat(
                messageRepository.findMessagesBySender(user.getId()).stream().map(Message::getRecipient),
                messageRepository.findMessagesByRecipient(user.getId()).stream().map(Message::getSender))
                .distinct()
                .map(userRepository::findUserById)
                .collect(Collectors.toList());
        return new ResponseEntity<>(usrs, HttpStatus.OK);
    }

    @GetMapping("/users/loggedin")
    public ResponseEntity<Boolean> userRecommendation() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
