package com.sparta.nbcampnewsfeed.repository;

import com.sparta.nbcampnewsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
