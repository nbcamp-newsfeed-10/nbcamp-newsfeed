package com.sparta.nbcampnewsfeed.repository;

import com.sparta.nbcampnewsfeed.entity.Friend;
import com.sparta.nbcampnewsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByToUserAndFromUser(User toUser, User fromUser);

    Optional<Friend> findByToUserAndFromUserAndAndFriendStatus(User toUser, User fromUser, boolean friendStatus);

    // 친구 목록 조회
    List<Friend> findAllByToUserAndFriendStatus(User toUser, boolean friendStatus);
}
