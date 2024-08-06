package com.example.kakao.security.jwt;

public class JwtProperties {

    public static Long ACCESS_EXPIRES_IN_MILLISECONDS = 60L*60*24*14*1000; // 2 weeks
    public static Long REFRESH_EXPIRES_IN_MILLISECONDS = 60L*60*24*28*1000; // 4 weeks

    public static String ACCESS_TOKEN = "access_token";
    public static String REFRESH_TOKEN = "refresh_token";
}
