package com.baykus.twitterclone.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfilBilgileri implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mesaj")
    @Expose
    private String mesaj;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("kullaniciadi")
    @Expose
    private String kullaniciadi;
    @SerializedName("adsoyad")
    @Expose
    private String adsoyad;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

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

    public String getAdsoyad() {
        return adsoyad;
    }

    public void setAdsoyad(String adsoyad) {
        this.adsoyad = adsoyad;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "ProfilBilgileri{" +
                "status='" + status + '\'' +
                ", mesaj='" + mesaj + '\'' +
                ", id='" + id + '\'' +
                ", kullaniciadi='" + kullaniciadi + '\'' +
                ", adsoyad='" + adsoyad + '\'' +
                ", mail='" + mail + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}

