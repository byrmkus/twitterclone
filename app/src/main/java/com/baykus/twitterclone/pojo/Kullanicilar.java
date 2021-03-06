package com.baykus.twitterclone.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kullanicilar {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("kullaniciadi")
    @Expose
    private String kullaniciadi;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("adsoyad")
    @Expose
    private String adsoyad;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKullaniciadi() {
        return kullaniciadi;
    }

    public void setKullaniciadi(String kullaniciadi) {
        this.kullaniciadi = kullaniciadi;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdsoyad() {
        return adsoyad;
    }

    public void setAdsoyad(String adsoyad) {
        this.adsoyad = adsoyad;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
