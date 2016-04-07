package com.scut.wwh.keystore.domain;
/**
 * Created by Administrator on 2016/4/6.
 */
public class KeyStore {
    public  String publicKey;
    public  String privateKey;
    public  String  date;
    public  String verify;
    public  User user;
    public String getPublicKey() {
        return publicKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public KeyStore() {

    }

    public KeyStore(String publicKey,String privateKey,String date,String verify,User user) {
        super();
        this.publicKey = publicKey;
        this.privateKey=privateKey;
        this.date=date;
        this.verify=verify;
        this.user=user;
    }

    @Override
    public String toString() {
        return "KeyStore{" +
                "publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", date='" + date + '\'' +
                ", verify='" + verify + '\'' +
                ", user=" + user +
                '}';
    }
}
