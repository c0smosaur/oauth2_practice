package com.example.kakao.security;

import com.example.kakao.common.RoleType;
import com.example.kakao.users.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class UsersDetails implements UserDetails {

    private final Users users;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        assert users != null;
        // single authority, (enum) role type
        RoleType role = users.getRoleType();
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return users.getEmail();
    }

    public Long getId() {
        return users.getId();
    }
}
