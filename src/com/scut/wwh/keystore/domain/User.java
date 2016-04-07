package com.scut.wwh.keystore.domain;

/**
 * Created by Administrator on 2016/4/4.
 */
public class User {
        public String username;
        public String password;

    public User() {
    }

    public User(String username,String password) {
        super();
        this.password=password;
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
