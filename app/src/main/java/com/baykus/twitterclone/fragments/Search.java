package com.baykus.twitterclone.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baykus.twitterclone.adapters.SearchAdapter;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.databinding.FragmentSearchBinding;
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
    private SearchAdapter searchAdapter;
    private KisilerDao kisilerDao;
    private List<Kullanicilar> kullanicilarList;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = mContext;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        searchBinding = null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        kullanicilarList = new ArrayList<>();
        kisilerDao = ApiUtils.getKisilerDao();
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        View v = searchBinding.getRoot();

        setAdapter();
        searchBinding.rvSearch.setAdapter(searchAdapter);
        searchAdapter=new SearchAdapter(mContext,kullanicilarList);

        id = getArguments().getString("id", "-1");
        Log.e("Search id ", id);

        search(getArguments().getString("search_text", ""));
        if (getArguments().getString("search_text", "") == null) {
            search("");
        } else
            search(getArguments().getString("search_text", ""));
        setAdapter();
        searchAdapter = new SearchAdapter(mContext, kullanicilarList);

        // Inflate the layout for this fragment
        return v;
    }




    public void search(String word) {
        kullanicilarList.clear();
        kisilerDao.kullaniciAra(id, word).enqueue(new Callback<SelectUsers>() {
            @Override
            public void onResponse(Call<SelectUsers> call, Response<SelectUsers> response) {

                kullanicilarList = response.body().getKullanicilar();

                try {

                    setAdapter();
                    if (kullanicilarList.size() == 0) {
                        kullanicilarList.clear();
                        searchBinding.txtSonucBulunamadi.setText("SONUC BULUNAMADI");
                    } else {
                        searchBinding.txtSonucBulunamadi.setText("ARANAN KULLANICILAR");

                        searchBinding.rvSearch.setAdapter(searchAdapter);
                        searchAdapter = new SearchAdapter(mContext, kullanicilarList);
                    }
                } catch (NullPointerException e) {

                    Log.e("NullPointException ", e.toString());
                   // searchBinding.txtSonucBulunamadi.setText("HiÇBİR SONUC BULUNAMADI");


                }
                searchAdapter.notifyDataSetChanged();
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