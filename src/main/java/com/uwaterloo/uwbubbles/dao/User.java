package com.uwaterloo.uwbubbles.dao;

import com.uwaterloo.uwbubbles.types.IntArrayUserType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Data
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
    private long id;
    private String username;
    private String name;
    private String email;
    private String password;

    // 0 - Arts
    // 1 - Engineering
    // 2 - Environment
    // 3 - Health
    // 4 - Mathematics
    // 5 - Science
    private int faculty;
    private int gender;
    private boolean enabled;

    @Type(type = "integer-array")
    @Column(
        name = "interests",
        columnDefinition = "integer[]"
    )
    private Integer[] interests;

    public User(String username,
                String email,
                String password,
                int gender,
                Integer[] interests,
                boolean enabled,
                String name,
                int faculty) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.interests = interests;
        this.enabled = enabled;
        this.faculty = faculty;
    }

    public User() {}
}
