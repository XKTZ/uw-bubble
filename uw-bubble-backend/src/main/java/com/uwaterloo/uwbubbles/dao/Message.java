package com.uwaterloo.uwbubbles.dao;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    long sender;
    long recipient;
    String content;
    String type;

    @CreationTimestamp
    Date date;

    public Message(long sender, long recipient, String content, String type, Date date) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.type = type;
        this.date = date;
    }
    public Message() {}
}
