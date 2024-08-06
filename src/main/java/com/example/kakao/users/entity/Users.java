package com.example.kakao.users.entity;

import com.example.kakao.common.BaseEntity;
import com.example.kakao.common.RoleType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users extends BaseEntity {

    private String email;
    private String password; // kakao user info의 id(long)를 암호화한 것
    private String profileImage;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // 하드웨어 번호
    @Setter
    private String hardwareNo;
}
