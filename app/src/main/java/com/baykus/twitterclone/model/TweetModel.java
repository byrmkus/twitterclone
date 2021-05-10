package com.baykus.twitterclone.model;

public class TweetModel {
    private String adSoyad;
    private String kullaniciAdi;
    private String profilPath;
    private String resimPath;
    private String tweetText;
    private String tarih;

    public TweetModel() {
    }

    public TweetModel(String adSoyad, String kullaniciAdi, String profilPath, String resimPath, String tweetText, String tarih) {
        this.adSoyad = adSoyad;
        this.kullaniciAdi = kullaniciAdi;
        this.profilPath = profilPath;
        this.resimPath = resimPath;
        this.tweetText = tweetText;
        this.tarih = tarih;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getProfilPath() {
        return profilPath;
    }

    public void setProfilPath(String profilPath) {
        this.profilPath = profilPath;
    }

    public String getResimPath() {
        return resimPath;
    }

    public void setResimPath(String resimPath) {
        this.resimPath = resimPath;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
}
