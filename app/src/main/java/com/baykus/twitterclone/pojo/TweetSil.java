package com.baykus.twitterclone.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TweetSil {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mesaj")
    @Expose
    private String mesaj;

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
