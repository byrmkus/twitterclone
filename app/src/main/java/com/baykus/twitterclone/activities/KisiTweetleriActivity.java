package com.baykus.twitterclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.baykus.twitterclone.R;
import com.baykus.twitterclone.adapters.TweetlerAdapter;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.databinding.ActivityKisiTweetleriBinding;
import com.baykus.twitterclone.databinding.ActivityProfilBinding;
import com.baykus.twitterclone.pojo.GetKisiTweetler;
import com.baykus.twitterclone.pojo.Tweetler;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KisiTweetleriActivity extends AppCompatActivity {
    private String id;
    ActivityKisiTweetleriBinding binding;
    private KisilerDao kisilerDao;
    private List<Tweetler> arananKullaniciList;
    private TweetlerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi_tweetleri);
        binding = ActivityKisiTweetleriBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        kisilerDao= ApiUtils.getKisilerDao();
        setContentView(view);
        Intent intent = getIntent();
        arananKullaniciList=new ArrayList<>();
        setAdapter();

        if (intent != null) {

            id = intent.getStringExtra("id");
            Log.e("Seçilen kullanıcı id ", id);
            Log.e("Seçilen kullanıcı adı ", intent.getStringExtra("kullaniciadi"));
            if (!intent.getStringExtra("path").equals("")) {
                Picasso.get().load(intent.getStringExtra("path"))
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(binding.profileImageKisi);
            }
            binding.kisiKullaniciadi.setText("@"+intent.getStringExtra("kullaniciadi"));
            binding.kisiAdsoyad.setText(intent.getStringExtra("adsoyad"));
            binding.kisiMail.setText(intent.getStringExtra("mail"));
            arananKullaniciTweet();
        } else
            Toast.makeText(this, "Seçilen kullanici bulunamadi...", Toast.LENGTH_SHORT).show();


    }

    private void arananKullaniciTweet() {
        kisilerDao.getKisiTweetler(id).enqueue(new Callback<GetKisiTweetler>() {
            @Override
            public void onResponse(Call<GetKisiTweetler> call, Response<GetKisiTweetler> response) {

                arananKullaniciList = response.body().getTweetler();

                if (arananKullaniciList.size()==0){
                    Toast.makeText(KisiTweetleriActivity.this, "Tweet Bulunamadı.", Toast.LENGTH_SHORT).show();

                }else {
                    adapter = new TweetlerAdapter(KisiTweetleriActivity.this, arananKullaniciList,false);
                    binding.kisitweetleriRv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GetKisiTweetler> call, Throwable t) {

            }
        });

    }
    private void setAdapter() {
        binding.kisitweetleriRv.setHasFixedSize(true);
        binding.kisitweetleriRv.setLayoutManager(new LinearLayoutManager(KisiTweetleriActivity.this));
    }
}