package com.uwaterloo.uwbubbles.dto;

import com.uwaterloo.uwbubbles.dao.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class RecommendationResponse implements Serializable {
    private final User pair;
    private final Double matchRate;
    public RecommendationResponse(User pair, Double matchRate) {
        this.pair = pair;
        this.matchRate = matchRate;
    }
}
