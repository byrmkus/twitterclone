package com.baykus.twitterclone.api;

import com.baykus.twitterclone.pojo.CRUDCevap;
import com.baykus.twitterclone.pojo.Login;
import com.baykus.twitterclone.pojo.ProfilFoto;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface KisilerDao {
    @POST("twitterclone/register.php")
    @FormUrlEncoded
    Call<CRUDCevap> kisiEkle(@Field("kullaniciadi") String kullaniciadi,
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

}
