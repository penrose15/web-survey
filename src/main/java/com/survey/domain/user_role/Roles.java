package com.survey.domain.user_role;

import lombok.Getter;

@Getter
public enum Roles {
    USER("USER"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN");

    private String role;

    Roles(String role) {
        this.role = role;
    }
}
