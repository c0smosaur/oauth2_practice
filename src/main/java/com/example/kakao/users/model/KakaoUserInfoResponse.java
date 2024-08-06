package com.example.kakao.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponse {
    private Long id;
    private LocalDateTime connectedAt;
    private String email;
    private String nickname;
    private String profileImage;
    private KakaoAccount kakaoAccount;

    public KakaoUserInfoResponse() {}

    public static UsersRequest toMemberRegisterRequest(KakaoUserInfoResponse userInfo) {
        return new UsersRequest(userInfo.getEmail(),
                String.valueOf(userInfo.getId()),
                userInfo.getProfileImage(),
                userInfo.getNickname());
    }

    public static KakaoUserInfoResponse fromJson(String jsonResponseBody, ObjectMapper objectMapper) throws IOException {
        KakaoUserInfoResponse userInfo = objectMapper.readValue(jsonResponseBody, KakaoUserInfoResponse.class);

        userInfo.email = userInfo.kakaoAccount.getEmail();
        userInfo.nickname = userInfo.kakaoAccount.getProfile().getNickname();
        userInfo.profileImage = userInfo.kakaoAccount.getProfile().getProfileImageUrl();
        return userInfo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        private boolean hasEmail;
        @Getter
        private String email;
        @Getter
        private Profile profile;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
        private String nickname;
        private String profileImageUrl;
    }
}
