package com.example.kakao.users.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record UsersResponse(String email,
                            String nickname,
                            String profileImage,
                            String hardwareNo){
}
