package com.baykus.twitterclone.api;

import com.baykus.twitterclone.pojo.GetKisiTweetler;
import com.baykus.twitterclone.pojo.Login;
import com.baykus.twitterclone.pojo.ProfilBilgileri;
import com.baykus.twitterclone.pojo.ProfilFoto;
import com.baykus.twitterclone.pojo.Register;
import com.baykus.twitterclone.pojo.SelectUsers;
import com.baykus.twitterclone.pojo.TweetSil;
import com.baykus.twitterclone.pojo.Tweetler;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface KisilerDao {
    @POST("twitterclone/register.php")
    @FormUrlEncoded
    Call<Register> kisiEkle(@Field("kullaniciadi") String kullaniciadi,
                            @Field("sifre") String sifre,
                            @Field("mail") String mail,
                            @Field("adsoyad") String adsoyad);


    @POST("twitterclone/login.php")
    @FormUrlEncoded
    Call<Login> kisiLogin(@Field("kullaniciadi") String kullaniciadi,
                          @Field("sifre") String sifre);


    @POST("twitterclone/profilFotoYukle.php")
    @FormUrlEncoded
    Call<ProfilFoto> profilFoto(@Field("id") String id,
                                @Field("avatar") String avatar);

    @GET("twitterclone/profilBilgileri.php")
    Call<ProfilBilgileri> profilBilgileri(@Query("id") String id);

    @POST("twitterclone/tweetler.php")
    @FormUrlEncoded
    Call<ProfilFoto> tweetYukle(@Field("id") String id,
                                @Field("uuid") String uuid,
                                @Field("istek_turu") String istek_turu,
                                @Field("text") String text,
                                @Field("resim") String path);

    @GET("twitterclone/getTweetler.php")
    Call<GetKisiTweetler> getKisiTweetler(@Query("id") String id);

    @POST("twitterclone/tweetler.php")
    @FormUrlEncoded
    Call<Tweetler> tweetler(@Field("id") String id);

    @POST("twitterclone/tweetSil.php")
    @FormUrlEncoded
    Call<TweetSil> tweetSil(@Field("uuid") String uuid);

    @POST("twitterclone/selectusers.php")
    @FormUrlEncoded
    Call<SelectUsers> kullaniciAra(@Field("id") String id,
                                   @Field("word") String avatar);
}
