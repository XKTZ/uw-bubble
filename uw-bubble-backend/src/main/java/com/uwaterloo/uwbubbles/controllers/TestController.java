package com.uwaterloo.uwbubbles.controllers;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@RestController
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users/gen")
    public void gen() {
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

        WebClient.RequestBodySpec requestSpec = (WebClient.RequestBodySpec) client.method(HttpMethod.GET)
                .uri("/user/random/")
                .bodyValue(Map.of("size", 1024));

        List<User> response = requestSpec.retrieve().bodyToMono(new ParameterizedTypeReference<List<AI>>() {
        }).block().stream().map(AI.toUser()).collect(Collectors.toList());

        userRepository.saveAll(response);
    }

    @GetMapping("/users/adjust")
    public void adjust(@RequestBody Integer times) {
        for (int i = 0; i < times; i ++) {
            HttpClient httpClient = HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000000)
                    .responseTimeout(Duration.ofSeconds(5000))
                    .doOnConnected(conn ->
                            conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.SECONDS))
                                    .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.SECONDS)));

            WebClient client = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl("http://192.168.43.235:5000")
                    .build();

            var usrs = userRepository.getUserByRandom64().stream().map(AI.toAI()).collect(Collectors.toList());

            var results = usrs.stream().flatMap(
                    usr -> usrs.stream().map(usr2 -> new AI.CommunicationResult(usr, usr2, AI.testMatch(usr, usr2)))
            ).collect(Collectors.toList());

            WebClient.RequestBodySpec requestSpec = (WebClient.RequestBodySpec) client.method(HttpMethod.GET)
                    .uri("/user/adjust/")
                    .bodyValue(results);


            requestSpec.retrieve().bodyToMono(new ParameterizedTypeReference<Boolean>() {
            }).block();
        }
    }

    @GetMapping("/users/test")
    @ResponseBody
    public List<Map.Entry<Double, Double>> test() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000000)
                .responseTimeout(Duration.ofSeconds(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.SECONDS)));

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://192.168.43.235:5000")
                .build();

        var usrs = userRepository.getUserByRandom64().stream().map(AI.toAI()).collect(Collectors.toList());
        var user = usrs.get(0);


        var correct = usrs.stream().map(usr -> new AbstractMap.SimpleEntry<>(usr.getId(), AI.testMatch(user, usr)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        WebClient.RequestBodySpec requestSpec = (WebClient.RequestBodySpec) client.method(HttpMethod.GET)
                .uri("/user/select/")
                .bodyValue(Map.of(
                        "from", user,
                        "to", usrs
                ));


        var result = requestSpec.retrieve().bodyToMono(new ParameterizedTypeReference<Map<Long, Double>>() {
        }).block();

        return result.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(correct.get(entry.getKey()), entry.getValue())).collect(Collectors.toList());
    }

    @GetMapping("/users/test-match")
    @ResponseBody
    public List<Double> testGender() {
        var users = userRepository.getUserByRandom64().stream().map(AI.toAI()).collect(Collectors.toList());
        return users.stream().flatMap(usr -> users.stream().map(usr2 -> AI.testMatch(usr, usr2))).collect(Collectors.toList());
    }
}
