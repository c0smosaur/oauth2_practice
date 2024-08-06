package com.example.kakao.users.controller;

import com.example.kakao.security.UsersDetails;
import com.example.kakao.users.converter.UsersConverter;
import com.example.kakao.users.entity.Users;
import com.example.kakao.users.model.KakaoUserInfoResponse;
import com.example.kakao.users.model.UsersResponse;
import com.example.kakao.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final UsersConverter usersConverter;

    @PostMapping("/login")
    public ResponseEntity<UsersResponse> login(@RequestBody KakaoUserInfoResponse userInfo){
        Users user = usersService.loginUser(
                KakaoUserInfoResponse.toMemberRegisterRequest(userInfo));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + usersService.issueAccessToken(user));

        return new ResponseEntity<>(usersConverter.toResponse(user), headers, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UsersResponse> userInfo(@AuthenticationPrincipal UsersDetails usersDetails){
        UsersResponse response = usersConverter.toResponse(
                usersService.getUserInfo(usersDetails)
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
