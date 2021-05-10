package com.baykus.twitterclone.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baykus.twitterclone.R;
import com.baykus.twitterclone.activities.TwitterNavDrawerActivity;
import com.baykus.twitterclone.adapters.SearchAdapter;
import com.baykus.twitterclone.adapters.TweetlerAdapter;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.databinding.FragmentSearchBinding;
import com.baykus.twitterclone.pojo.GetKisiTweetler;
import com.baykus.twitterclone.pojo.Kullanicilar;
import com.baykus.twitterclone.pojo.SelectUsers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search extends Fragment {

    private Context mContext;
    private String id;
    private FragmentSearchBinding searchBinding;
    private SearchAdapter adapter;
    private KisilerDao kisilerDao;
    private List<Kullanicilar> kullanicilarList;
    TwitterNavDrawerActivity t;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        searchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        View v = searchBinding.getRoot();
        kullanicilarList = new ArrayList<>();
        setAdapter();
        id = getArguments().getString("id", "-1");
        Log.e("Search id ", id);

        kisilerDao = ApiUtils.getKisilerDao();

         t = new TwitterNavDrawerActivity();

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        searchBinding = null;
    }

    public void search(String word) {
        kisilerDao.kullaniciAra(id, word).enqueue(new Callback<SelectUsers>() {
            @Override
            public void onResponse(Call<SelectUsers> call, Response<SelectUsers> response) {
                kullanicilarList = response.body().getKullanicilar();
                Log.e("Search users :", kullanicilarList.toString());
                setAdapter();
                if (kullanicilarList.size() == 0) {

                } else {
                    adapter = new SearchAdapter(mContext, kullanicilarList);
                    searchBinding.rvSearch.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<SelectUsers> call, Throwable t) {

            }
        });


    }

    private void setAdapter() {
        searchBinding.rvSearch.setHasFixedSize(true);
        searchBinding.rvSearch.setLayoutManager(new LinearLayoutManager(mContext));
    }
}