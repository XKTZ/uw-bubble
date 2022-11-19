package com.uwaterloo.uwbubbles.services;

import com.uwaterloo.uwbubbles.dao.Message;
import com.uwaterloo.uwbubbles.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public void saveMessage(Message msg) {
        messageRepository.save(msg);
    }

    public List<Message> findMessagesFor(long id) {
        return messageRepository.findMessagesBySender(id);
    }

}
