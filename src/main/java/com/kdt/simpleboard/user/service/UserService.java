package com.kdt.simpleboard.user.service;

import com.kdt.simpleboard.user.domain.User;
import com.kdt.simpleboard.user.dto.UserMapper;
import com.kdt.simpleboard.user.dto.UserRequest;
import com.kdt.simpleboard.user.dto.UserResponse;
import com.kdt.simpleboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserResponse.SignUpRes createUser(UserRequest.SignUpReq request) {
        User user = userRepository.save(UserMapper.toUser(request));
        return UserMapper.toSignUpRes(user);
    }
}