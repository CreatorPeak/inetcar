package com.inetcar.model;


import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private int uid;
    private String uname;
    private String phone;
    private String passwd;
    private String photo;

    public User() {

    }
    public User(int uid, String uname, String phone, String passwd, String photo) {
        super();
        this.uid = uid;
        this.uname = uname;
        this.phone = phone;
        this.passwd = passwd;
        this.photo = photo;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}