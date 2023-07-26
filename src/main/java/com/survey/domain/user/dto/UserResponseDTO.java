package com.survey.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDTO {
    private Long id;
    private String email;

    @Builder
    public UserResponseDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
