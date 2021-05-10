
package com.baykus.twitterclone.pojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetKisiTweetler {

    @SerializedName("tweetler")
    @Expose
    private List<Tweetler> tweetler = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mesaj")
    @Expose
    private String mesaj;

    public List<Tweetler> getTweetler() {
        return tweetler;
    }

    public void setTweetler(List<Tweetler> tweetler) {
        this.tweetler = tweetler;
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

    @Override
    public String toString() {
        return "GetKisiTweetler{" +
                "tweetler=" + tweetler +
                ", status='" + status + '\'' +
                ", mesaj='" + mesaj + '\'' +
                '}';
    }
}
