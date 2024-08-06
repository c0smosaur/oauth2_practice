package com.example.kakao.users.service;

import com.example.kakao.security.UsersDetails;
import com.example.kakao.users.converter.UsersConverter;
import com.example.kakao.users.entity.Users;
import com.example.kakao.users.model.UsersRequest;
import com.example.kakao.users.repository.UsersRepository;
import com.example.kakao.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.kakao.security.jwt.JwtProperties.*;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final JwtProvider jwtProvider;
    private final UsersConverter usersConverter;

    // 카카오 로그인 -> 연동 여부에 따라 회원정보 db에 저장
    public Users loginUser(UsersRequest request) {
        if (isExistingUser(request)){
            return usersRepository.findByEmail(request.email());
        } else {
            return registerUser(request);
        }
    }

    // 등록 x인 회원 정보 저장
    private Users registerUser(UsersRequest request){
        String password = passwordEncoder.encode(request.password());
        Users users = usersConverter.toEntity(request, password);
        Users newUsers = usersRepository.save(users);
        return newUsers;
    }

    // 이미 등록된 회원인지 체크
    private boolean isExistingUser(UsersRequest request){
        if (usersRepository.existsByEmail(request.email())){
            return true;
        } else {
            return false;
        }
    }

    // 액세스 토큰 (2주) 발급
    public String issueAccessToken(Users users){
        return jwtProvider.createToken(users.getId(), ACCESS_TOKEN);
    }

    // 회원정보 가져오기
    public Users getUserInfo(UsersDetails usersDetails){
        return usersRepository.findFirstById(usersDetails.getId());
    }

}
