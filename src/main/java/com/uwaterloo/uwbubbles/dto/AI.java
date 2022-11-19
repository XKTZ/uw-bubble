package com.uwaterloo.uwbubbles.dto;

import lombok.Data;

import java.io.Serializable;

@Data

public class AI implements Serializable {
    long id;
    int gender;
    Integer[] interests;
    int faculty;

    //need default constructor for JSON Parsing
    public AI() {

    }

    public AI(long id, int gender, Integer[] interests, int faculty) {
       this.id = id;
       this.gender = gender;
       this.interests = interests;
       this.faculty = faculty;
    }

}
