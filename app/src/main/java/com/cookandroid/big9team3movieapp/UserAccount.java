package com.cookandroid.big9team3movieapp;

import com.google.firebase.database.FirebaseDatabase;

//사용자 계정 정보 모델 클래스
public class UserAccount {
    private String idToken; // Firebase Uid 고유 토큰 정보
    private String emailId;
    private String password;
    private String name;
    private String phone;

    public UserAccount() {
       //firebase realtime 쓸려면 constructor무조건 생성!!
    }

    FirebaseDatabase mDatabase;
    public UserAccount(String idToken, String emailId, String name){
        this.idToken = idToken;
        this.emailId = emailId;
        this.name = name;
    }

    public UserAccount(String newname) {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
