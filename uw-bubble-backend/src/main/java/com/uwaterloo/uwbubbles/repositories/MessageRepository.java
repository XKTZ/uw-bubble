package com.uwaterloo.uwbubbles.repositories;

import com.uwaterloo.uwbubbles.dao.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public List<Message> findMessagesBySenderAndRecipient(long id, long id2);

    public List<Message> findMessagesBySender(long id);

    public List<Message> findMessagesByRecipient(long id);

    public List<Message> findMessagesByDateAfter(Date date);
}
