package com.survey.global.adapter;

import com.survey.domain.user.entity.Roles;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class UserAdapter extends User {

    private com.survey.domain.user.entity.User user;

    public UserAdapter(com.survey.domain.user.entity.User user) {
        super(user.getUsername(), user.getPassword(), authorities(user.getRole()));
    }

    private static Collection<? extends GrantedAuthority> authorities(Roles roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority(roles.getRole());
        authorities.add(auth);

        return authorities;
    }
}
