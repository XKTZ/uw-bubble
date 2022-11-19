package com.uwaterloo.uwbubbles.repositories;

import com.uwaterloo.uwbubbles.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findUserByName(String s);
    public User findUserById(long id);
    public User findUserByEmail(String s);
    @Query(value = "SELECT * FROM account.users ORDER BY random() LIMIT 64", nativeQuery = true)
    public List<User> getUserByRandom64();
}
