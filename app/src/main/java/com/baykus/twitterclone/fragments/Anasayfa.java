package com.baykus.twitterclone.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baykus.twitterclone.adapters.TweetlerAdapter;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.databinding.FragmentAnasayfaBinding;
import com.baykus.twitterclone.pojo.GetKisiTweetler;
import com.baykus.twitterclone.pojo.Tweetler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Anasayfa extends Fragment {
    FragmentAnasayfaBinding anasayfaBinding;
    private String id;
    private Context mContext;
    private RecyclerView recyclerView ;
    private List<Tweetler> tweetlerList;
    private TweetlerAdapter adapter;
    private KisilerDao kisilerDao;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        anasayfaBinding = FragmentAnasayfaBinding.inflate(inflater, container, false);
        View view = anasayfaBinding.getRoot();
        tweetlerList = new ArrayList<>();

        setAdapter();

        id = getArguments().getString("id", "-1");
        Log.e("Anasayfa ", id);

        kisilerDao = ApiUtils.getKisilerDao();
        istekGonder();
        anasayfaBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                tweetlerList.clear();
                setAdapter();
                anasayfaBinding.listTweet.setItemAnimator(new DefaultItemAnimator());
                istekGonderRefresh();
            }
        });
        return view;
    }

    private void setAdapter() {
        anasayfaBinding.listTweet.setHasFixedSize(true);
        anasayfaBinding.listTweet.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void istekGonder() {
        kisilerDao.getKisiTweetler(id).enqueue(new Callback<GetKisiTweetler>() {
            @Override
            public void onResponse(Call<GetKisiTweetler> call, Response<GetKisiTweetler> response) {
                tweetlerList = response.body().getTweetler();
                Log.e("Jsonlar :", tweetlerList.toString());
                if (tweetlerList.size()==0){
                    anasayfaBinding.txt.setText("Tweet bulunamadı..");
                }else {
                    adapter = new TweetlerAdapter(mContext, tweetlerList,false);
                    anasayfaBinding.listTweet.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GetKisiTweetler> call, Throwable t) {

            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        anasayfaBinding = null;
    }


    private void istekGonderRefresh() {
        kisilerDao.getKisiTweetler(id).enqueue(new Callback<GetKisiTweetler>() {
            @Override
            public void onResponse(Call<GetKisiTweetler> call, Response<GetKisiTweetler> response) {

                anasayfaBinding.swipeRefresh.setRefreshing(false);

                tweetlerList = response.body().getTweetler();
                Log.e("Jsonlar :", tweetlerList.toString());
                if (tweetlerList.size()==0){
                    anasayfaBinding.txt.setText("Tweet bulunamadı..");

                    adapter = new TweetlerAdapter(mContext, tweetlerList,false);
                    anasayfaBinding.listTweet.setAdapter(adapter);
                }else {
                    adapter = new TweetlerAdapter(mContext, tweetlerList,false);
                    anasayfaBinding.listTweet.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GetKisiTweetler> call, Throwable t) {
                anasayfaBinding.swipeRefresh.setRefreshing(false);

            }
        });

    }


}