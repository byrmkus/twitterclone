package com.baykus.twitterclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baykus.twitterclone.databinding.KisiCardTasarimBinding;
import com.baykus.twitterclone.pojo.Kullanicilar;
import com.baykus.twitterclone.pojo.Tweetler;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CardTasarimTutucu> {
    private Context mContext;
    private List<Kullanicilar> kullanicilarList;

    public SearchAdapter(Context mContext, List<Kullanicilar> kullanicilarList) {
        this.mContext = mContext;
        this.kullanicilarList = kullanicilarList;
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        KisiCardTasarimBinding kisiCardTasarimBinding = KisiCardTasarimBinding.inflate(inflater,parent,false);

        return new CardTasarimTutucu(kisiCardTasarimBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, int position) {


        Kullanicilar kullanicilar = kullanicilarList.get(position);
        holder.binding.aramaAdsoyad.setText(kullanicilar.getAdsoyad());
        holder.binding.aramaMail.setText(kullanicilar.getMail());
        holder.binding.aramaKullaniciadi.setText("@"+kullanicilar.getKullaniciadi());


        if (!kullanicilar.getAvatar().equals("")){
            Picasso.get().load(kullanicilar.getAvatar())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.binding.profileImageArama);
        }

    }

    @Override
    public int getItemCount() {
        return kullanicilarList.size();
    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder {
        private KisiCardTasarimBinding binding;

        public CardTasarimTutucu(@NonNull KisiCardTasarimBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
