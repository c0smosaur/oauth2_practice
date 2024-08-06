package com.example.kakao.users.converter;

import com.example.kakao.common.RoleType;
import com.example.kakao.users.entity.Users;
import com.example.kakao.users.model.UsersRequest;
import com.example.kakao.users.model.UsersResponse;
import org.springframework.stereotype.Component;

@Component
public class UsersConverter {

    public Users toEntity(UsersRequest request, String password){
        return Users.builder()
                .email(request.email())
                .password(password)
                .profileImage(request.profileImage())
                .nickname(request.nickname())
                .roleType(RoleType.ROLE_USER)
                .hardwareNo(null)
                .build();
    }

    public UsersResponse toResponse(Users users){
        return UsersResponse.builder()
                .email(users.getEmail())
                .profileImage(users.getProfileImage())
                .nickname(users.getNickname())
                .hardwareNo(users.getHardwareNo())
                .build();
    }
}
