package com.sparta.nbcampnewsfeed.service;

import com.sparta.nbcampnewsfeed.dto.request.CommentRequestDto;
import com.sparta.nbcampnewsfeed.dto.response.CommentResponseDto;
import com.sparta.nbcampnewsfeed.entity.User;
import com.sparta.nbcampnewsfeed.repository.CommentRepository;
import com.sparta.nbcampnewsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentResponseDto createComment(CommentRequestDto commentRequestDto) {
        User user = userRepository.findById
    }
}
