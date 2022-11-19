package com.uwaterloo.uwbubbles.dto;

import com.uwaterloo.uwbubbles.dao.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data

public class RecommendationRequest implements Serializable {
    private final User from;
    private final List<User> to;
    public RecommendationRequest(User from, List<User> to) {
        this.from = from;
        this.to = to;
    }
}
