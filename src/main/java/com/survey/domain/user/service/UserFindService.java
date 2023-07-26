package com.survey.domain.user.service;

import com.survey.domain.user.entity.User;
import com.survey.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserFindService {
    private final UserRepository userRepository;

    //유저 찾는 로직
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 email"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));
    }
}
