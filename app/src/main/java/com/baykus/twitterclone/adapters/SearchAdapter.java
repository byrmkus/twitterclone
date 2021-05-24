package com.baykus.twitterclone.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.baykus.twitterclone.activities.KisiTweetleriActivity;
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
        if (kullanicilarList.size() == 0)
            return null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        KisiCardTasarimBinding kisiCardTasarimBinding = KisiCardTasarimBinding.inflate(inflater, parent, false);


        return new CardTasarimTutucu(kisiCardTasarimBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, int position) {

        Kullanicilar kullanicilar = kullanicilarList.get(position);
        holder.binding.aramaAdsoyad.setText(kullanicilar.getAdsoyad());
        holder.binding.aramaMail.setText(kullanicilar.getMail());
        holder.binding.aramaKullaniciadi.setText("@" + kullanicilar.getKullaniciadi());


        if (!kullanicilar.getAvatar().equals("")) {
            Picasso.get().load(kullanicilar.getAvatar())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.binding.profileImageArama);
        }

        holder.binding.cardViewArama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("**************","********************");
                Log.e("seçilen kullanıcı id ",kullanicilar.getId());
                Intent intent =new Intent(v.getContext(),KisiTweetleriActivity.class);
                intent.putExtra("id", kullanicilar.getId());
                intent.putExtra("path", kullanicilar.getAvatar());
                intent.putExtra("kullaniciadi", kullanicilar.getKullaniciadi());
                intent.putExtra("adsoyad", kullanicilar.getAdsoyad());
                intent.putExtra("mail", kullanicilar.getMail());

                // Lolipop öncesi sürümlerde bu animasyon çalışmaz
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    View profil = holder.binding.profileImageArama;
                    View adSoyad = holder.binding.aramaAdsoyad;
                    View kullaniciAdi = holder.binding.aramaKullaniciadi;
                    Pair<View, String> pairProfilFoto = Pair.create(profil, "profilImage");
                    Pair<View, String> pairAdsoyad = Pair.create(adSoyad, "adsoyad");
                    Pair<View, String> pairKullaniciadi = Pair.create(kullaniciAdi, "kullaniciadi");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) mContext,
                            pairProfilFoto,
                            pairAdsoyad,
                            pairKullaniciadi);
                    mContext.startActivity(intent, options.toBundle());

                } else {
                    v.getContext().startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (kullanicilarList.size() == 0 || kullanicilarList == null)
            return 0;
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
