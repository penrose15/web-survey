package com.survey.domain.user.service;

import com.survey.domain.user.dto.UserResponseDTO;
import com.survey.domain.user.repository.UserRepository;
import com.survey.domain.user.dto.UserRequestDto;
import com.survey.domain.user.entity.User;
import com.survey.domain.user_role.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.desktop.OpenFilesEvent;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFindService userFindService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(UserRequestDto dto) {
        duplicateEmail(dto.getEmail());

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Roles.USER)
                .build()).getId();
    }
    //email 중복 방지
    private void duplicateEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()) throw new IllegalStateException("이미 존재하는 email");
    }

    public UserResponseDTO getUserInfo(String email) {
        User user = userFindService.findByEmail(email);
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
