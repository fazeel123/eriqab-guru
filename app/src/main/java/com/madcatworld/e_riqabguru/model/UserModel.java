package com.madcatworld.e_riqabguru.model;

public class UserModel {

    private String email;
    private String AccessToken;

    public UserModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        this.AccessToken = accessToken;
    }
}
