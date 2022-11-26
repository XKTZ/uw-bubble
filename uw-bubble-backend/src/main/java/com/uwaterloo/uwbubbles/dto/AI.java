package com.uwaterloo.uwbubbles.dto;

import com.uwaterloo.uwbubbles.dao.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.RandomStringGenerator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
public class AI implements Serializable {
    long id;
    int gender;
    int age;
    Integer[] interests;
    int program;

    //need default constructor for JSON Parsing
    public AI() {

    }


    public static Function<User, AI> toAI() {
        return (usr) -> new AI(usr.getId(), usr.getGender(), usr.getAge(), usr.getInterests(), usr.getFaculty());
    }


    public static Function<AI, User> toUser() {
        return (ai) -> new User(
                null,
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                ai.getProgram(),
                ai.getGender(),
                ai.getAge(),
                false,
                ai.getInterests()
        );
    }

    private static double sigmoid(double x) {
        return 1. / (1. + Math.exp(-x));
    }

    public static double testMatch(AI a, AI b) {
        double ageFactor = 0.4 * sigmoid(Math.abs(a.getAge() - b.getAge()) - 2);
        var ai = a.getInterests();
        var bi = b.getInterests();
        double interest = 0.6 * Math.tanh(
                IntStream.range(0, ai.length)
                        .mapToDouble(idx -> ai[idx] == 1 && bi[idx] == 1 ? 0.8 : (Math.max(ai[idx], bi[idx]) == 1 ? -0.2 : 0))
                        .sum()
        );
        double prog = 0.3 * Math.atan(a.getProgram() - b.getProgram());
        double gender = a.getGender() == b.getGender() ? 0.5 : -0.2;
        return sigmoid(ageFactor + interest + prog + gender);
    }

    @Data
    @AllArgsConstructor
    public static class CommunicationResult {
        public AI from;
        public AI to;
        public Double score;
    }
}
