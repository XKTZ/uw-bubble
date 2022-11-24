package com.uwaterloo.uwbubbles.services;

import com.uwaterloo.uwbubbles.dao.Message;
import com.uwaterloo.uwbubbles.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public void saveMessage(Message msg) {
        messageRepository.save(msg);
    }

    public List<Message> findMessagesBetween(long from, long to) {
        return Stream.concat(
                        messageRepository.findMessagesBySenderAndRecipient(from, to).stream(),
                        messageRepository.findMessagesBySenderAndRecipient(to, from).stream())
                .sorted(Comparator.comparing(Message::getDate)).collect(Collectors.toList());
    }

}
