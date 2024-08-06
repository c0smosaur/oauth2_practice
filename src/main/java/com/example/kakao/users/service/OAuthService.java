package com.example.kakao.users.service;

import com.example.kakao.users.model.OAuthTokenResponse;
import com.example.kakao.users.model.KakaoUserInfoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kakao.api-key}")
    private String API_KEY;
    @Value("${kakao.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${kakao.secret-key}")
    private String SECRET_KEY;

//    private String AUTHORIZE_URI = "https://kauth.kakao.com/oauth/authorize";
    private String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private String INFO_URI = "https://kapi.kakao.com/v2/user/me";

    public OAuthTokenResponse getKakaoToken(String authCode) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", API_KEY);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", authCode);
        params.add("client_secret", SECRET_KEY);

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> token = restTemplate.exchange(
                TOKEN_URI, HttpMethod.POST, kakaoTokenRequest, String.class);

        log.info(token.getBody());

        OAuthTokenResponse tokenResponse = objectMapper.readValue(token.getBody(), OAuthTokenResponse.class);
        log.info("accessToken: "+tokenResponse.getAccessToken());

        return tokenResponse;
    }

    public KakaoUserInfoResponse kakaoUserInfo(String accessToken) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String,String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                INFO_URI, HttpMethod.GET, kakaoUserInfoRequest, String.class);

        KakaoUserInfoResponse userInfo = KakaoUserInfoResponse.fromJson(response.getBody(), objectMapper);
        return userInfo;
    }
}
