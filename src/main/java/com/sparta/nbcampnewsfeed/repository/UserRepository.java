package com.sparta.nbcampnewsfeed.repository;

import com.sparta.nbcampnewsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // username 으로 User 를 찾는 메서드
    Optional<User> findByUsername(String username);
}
