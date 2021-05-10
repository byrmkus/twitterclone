
package com.baykus.twitterclone.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tweetler {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("adsoyad")
    @Expose
    private String adsoyad;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("kullaniciadi")
    @Expose
    private String kullaniciadi;
    @SerializedName("mail")
    @Expose
    private String mail;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    @Override
    public String toString() {
        return "Tweetler{" +
                "date='" + date + '\'' +
                ", id=" + id +
                ", uuid='" + uuid + '\'' +
                ", path='" + path + '\'' +
                ", text='" + text + '\'' +
                ", adsoyad='" + adsoyad + '\'' +
                ", avatar='" + avatar + '\'' +
                ", kullaniciadi='" + kullaniciadi + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
