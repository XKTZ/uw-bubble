package com.uwaterloo.uwbubbles.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.uwaterloo.uwbubbles.types.IntArrayUserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "users")
@TypeDefs({
    @TypeDef(
        name = "integer-array",
        typeClass = IntArrayUserType.class
    )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String name;
    private String email;
    @JsonIgnore
    private String password;

    // 0 - Arts
    // 1 - Engineering
    // 2 - Environment
    // 3 - Health
    // 4 - Mathematics
    // 5 - Science
    private int faculty;
    private int gender;

    private int age;
    private boolean enabled;

    @Type(type = "integer-array")
    @Column(
        name = "interests",
        columnDefinition = "integer[]"
    )
    private Integer[] interests;

    public User() {}

    @JsonSetter
    public void setInterests(List<Integer> interests) {
        this.interests = interests.toArray(Integer[]::new);
    }
}
