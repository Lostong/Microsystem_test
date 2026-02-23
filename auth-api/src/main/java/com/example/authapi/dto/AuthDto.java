package com.example.authapi.dto;

import lombok.Data;

public class AuthDto {

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class TokenResponse {
        private String token;
        public TokenResponse(String token) {
            this.token = token;
        }
    }

    @Data
    public static class ProcessRequest {
        private String text;
    }

    @Data
    public static class ProcessResponse {
        private String result;
        public ProcessResponse(String result) {
            this.result = result;
        }
    }
}