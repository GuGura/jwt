package com.cos.jwt.config.jwt;

public interface JwtProperties {
    String SECRET = "cos";//우리 서버만 알고 잇는 비밀값
    int EXPIRATION_TIME = 60000*10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
