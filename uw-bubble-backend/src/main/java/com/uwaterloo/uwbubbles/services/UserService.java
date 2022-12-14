package com.uwaterloo.uwbubbles.services;

import com.uwaterloo.uwbubbles.config.JwtTokenUtil;
import com.uwaterloo.uwbubbles.config.JwtUserDetailsService;
import com.uwaterloo.uwbubbles.dao.User;
import com.uwaterloo.uwbubbles.dto.AI;
import com.uwaterloo.uwbubbles.dto.RecommendationRequest;
import com.uwaterloo.uwbubbles.repositories.UserRepository;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MailService mailService;

    public boolean credentialsExist(User user) {
        return usernameExist(user.getUsername()) && emailExist(user.getEmail());
    }

    public boolean usernameExist(String username) {
        return userRepository.findUserByUsername(username) != null;
    }

    public boolean emailExist(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    public boolean enabled(String username) {
        User exp = userRepository.findUserByUsername(username);
        return exp.isEnabled();
    }

    public void enableUser(String username) {
        User exp = userRepository.findUserByUsername(username);
        exp.setEnabled(true);
        userRepository.save(exp);
    }

    // map<key, val>, key is user's id and val is [0, 1], 0 -> not recommended, 1 -> strongly recommend for the given user
    public Map<Long, Double> getRecommendedUsers(User user) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://192.168.43.235:5000")
                .build();

        var toAI = AI.toAI();

        AI aiUser = toAI.apply(user);
        List<AI> randomUsers = getRandomUsers().stream().map(toAI).collect(Collectors.toList());

        WebClient.RequestBodySpec requestSpec = (WebClient.RequestBodySpec) client.method(HttpMethod.GET)
                                                            .uri("/user/select/")
                                                            .bodyValue(new RecommendationRequest(aiUser, randomUsers));

        Map<String, Double> response = requestSpec.retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Double>>() {}).block();
        assert response != null;
        Map<Long, Double> res = new HashMap<>();
        for (String k : response.keySet()) {
            res.put(Long.parseLong(k), response.get(k));
        }
        return res;
    }
    public String sendVerificationEmail(User user)  {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtil.generateVerificationToken(userDetails);
        String confirmationUrl = "http://192.168.43.53:8080/users/verify-account/" + token;
        String body = "Please click the verification link below to verify your account" + "\r\n" + confirmationUrl + "\r\n" + "Thank you,\n" + "HashPie Team";
        try {
            mailService.sendEmail("tommypang04@gmail.com", user.getEmail(), body);
            return "Mail Sent";
        }
        catch (Exception e) {
            return "Unknown Error";
        }
    }
    public List<User> getRandomUsers()  {
        return userRepository.getUserByRandom64();
    }

    public User getUserFromJwt(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        String name = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return userRepository.findUserByUsername(name);
    }

}
