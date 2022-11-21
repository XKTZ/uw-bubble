package com.uwaterloo.uwbubbles.dao;

import com.uwaterloo.uwbubbles.types.IntArrayUserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

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
}
