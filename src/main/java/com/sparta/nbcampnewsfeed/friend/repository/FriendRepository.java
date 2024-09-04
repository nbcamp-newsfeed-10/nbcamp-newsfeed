package com.sparta.nbcampnewsfeed.friend.repository;

import com.sparta.nbcampnewsfeed.friend.entity.Friend;
import com.sparta.nbcampnewsfeed.profile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByToUserAndFromUser(User toUser, User fromUser);

    Optional<Friend> findByToUserAndFromUserAndFriendStatus(User toUser, User fromUser, boolean friendStatus);

    // 내가 받은 친구 요청 중 친구 상태가 true인 경우 (toUser가 본인인 경우)
    List<Friend> findAllByToUserAndFriendStatus(User toUser, boolean friendStatus);

    // 내가 보낸 친구 요청 중 친구 상태가 true인 경우 (fromUser가 본인인 경우)
    List<Friend> findAllByFromUserAndFriendStatus(User fromUser, boolean friendStatus);
}
