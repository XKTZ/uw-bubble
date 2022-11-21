package com.uwaterloo.uwbubbles.dto;

import com.uwaterloo.uwbubbles.dao.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data

public class RecommendationRequest implements Serializable {
    private final AI from;
    private final List<AI> to;
    public RecommendationRequest(AI from, List<AI> to) {
        this.from = from;
        this.to = to;
    }
}
