package com.uwaterloo.uwbubbles.services;

import com.uwaterloo.uwbubbles.dto.AI;
import com.uwaterloo.uwbubbles.dto.Pair;
import com.uwaterloo.uwbubbles.repositories.MessageRepository;
import com.uwaterloo.uwbubbles.repositories.UserRepository;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class TrainService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public MessageRepository messageRepository;

    //    @Scheduled(cron = "0 0 0 * *")
    @Scheduled(cron = "0 0 * * * ?")
    public void initialize() {
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

        var messages = messageRepository.findMessagesByDateAfter(
                ((Supplier<Date>) () -> {
                    var c = Calendar.getInstance();
                    c.add(Calendar.DATE, -1);
                    return c.getTime();
                }).get()
        ).stream().map(x ->
                Pair.of(Pair.of(Math.max(x.getSender(), x.getRecipient()), Math.min(x.getSender(), x.getRecipient())), x.getContent().length())
        ).collect(Collectors.groupingBy(x -> x.first))
                .entrySet()
                .stream()
                .map(x -> Pair.of(x.getKey(), x.getValue().stream().reduce(0, (a, b) -> a + b.second, Integer::sum)))
                .collect(Collectors.toList());

        if (messages.isEmpty()) {
            return;
        }

        var mx = messages.stream().max(Comparator.comparingInt(x -> x.second)).get().second;

        var results = messages.stream().map(x ->
                new AI.CommunicationResult(
                        AI.toAI().apply(userRepository.findUserById(x.first.first)),
                        AI.toAI().apply(userRepository.findUserById(x.first.second)),
                        1.0 * x.second / mx))
                .collect(Collectors.toList());

        WebClient.RequestBodySpec requestSpec = (WebClient.RequestBodySpec) client.method(HttpMethod.GET)
                .uri("/user/adjust/")
                .bodyValue(results);


        requestSpec.retrieve().bodyToMono(new ParameterizedTypeReference<Boolean>() {
        }).block();
    }
}
