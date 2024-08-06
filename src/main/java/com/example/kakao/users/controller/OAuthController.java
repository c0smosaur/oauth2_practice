package com.example.kakao.users.controller;

import com.example.kakao.users.model.KakaoUserInfoResponse;
import com.example.kakao.users.model.OAuthTokenResponse;
import com.example.kakao.users.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class OAuthController {

    private final OAuthService OAuthService;

    @GetMapping("/login/oauth2/code/kakao")
    public KakaoUserInfoResponse kakaoLogin(@RequestParam("code") String code) throws IOException {
        log.info("code: "+code);
        OAuthTokenResponse tokenResponse = OAuthService.getKakaoToken(code);
        String accessToken = tokenResponse.getAccessToken();
        KakaoUserInfoResponse kakaoUserInfo = OAuthService.kakaoUserInfo(accessToken);

        return kakaoUserInfo;
    }
}
