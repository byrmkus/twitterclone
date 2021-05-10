package com.baykus.twitterclone.pojo;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SelectUsers {
    @SerializedName("kullanicilar")
    @Expose
    private List<Kullanicilar> kullanicilar = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mesaj")
    @Expose
    private String mesaj;

    public List<Kullanicilar> getKullanicilar() {
        return kullanicilar;
    }

    public void setKullanicilar(List<Kullanicilar> kullanicilar) {
        this.kullanicilar = kullanicilar;
    }

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
}
