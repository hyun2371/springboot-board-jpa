package com.kdt.simpleboard.user.service;

import com.kdt.simpleboard.common.exception.CustomException;
import com.kdt.simpleboard.user.domain.User;
import com.kdt.simpleboard.user.dto.CreateUserRequest;
import com.kdt.simpleboard.user.dto.CreateUserResponse;
import com.kdt.simpleboard.user.dto.UserMapper;
import com.kdt.simpleboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kdt.simpleboard.common.exception.ErrorCode.*;
import static com.kdt.simpleboard.common.exception.ErrorCode.NOT_EXIST_USER_ID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByName(request.name())) {
            throw new CustomException(USER_ALREADY_EXISTS);
        }
        User user = userRepository.save(UserMapper.toUser(request));
        return UserMapper.toCreateUserRes(user);
    }

    @Transactional(readOnly = true)
    public User getUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_EXIST_USER_ID));
    }
}
