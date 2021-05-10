package com.baykus.twitterclone.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.databinding.TweetCardTasarimBinding;
import com.baykus.twitterclone.pojo.TweetSil;
import com.baykus.twitterclone.pojo.Tweetler;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetlerAdapter extends RecyclerView.Adapter<TweetlerAdapter.CardTasarimTutucu>  {
    private Context mContext;
    private List<Tweetler> tweetlerList;

    public TweetlerAdapter(Context mContext, List<Tweetler> tweetlerList) {
        this.mContext = mContext;
        this.tweetlerList = tweetlerList;
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TweetCardTasarimBinding tweetCardTasarimBinding = TweetCardTasarimBinding.inflate(inflater,parent,false);


        return new CardTasarimTutucu(tweetCardTasarimBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, int position) {

        Tweetler tweet = tweetlerList.get(position);
        holder.binding.txtAdSoyad.setText(tweet.getAdsoyad());
        holder.binding.tweetText.setText(tweet.getText());
        holder.binding.txtKullaniciAdi.setText("@"+tweet.getKullaniciadi());


        if (!tweet.getAvatar().equals("")){
            Picasso.get().load(tweet.getAvatar())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.binding.profileImageTweet);
        }
        if (!tweet.getPath().equals("")){
            Picasso.get().load(tweet.getPath()).into(holder.binding.imageTweet);
        }

        dateFormat(holder,tweet);
holder.binding.tweetCardView.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        holder.binding.tweetCardView.setAlpha(.5f);

        new AlertDialog.Builder(mContext)
                .setTitle("Tweet Sil")
                .setMessage("Tweeti silmek istediğinizden emin misiniz?")
                .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tweetSil(position,tweet.getUuid());

                    }
                }).setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.binding.tweetCardView.setAlpha(1.0f);

            }
        }).show();

        return true;
    }
});


    }

    private void tweetSil(int position, String uuid) {

        ProgressDialog loading = ProgressDialog.show(mContext, "Tweet Siliniyor",
                "Lütfen bekleyiniz...", false, true);
        KisilerDao kisilerDao = ApiUtils.getKisilerDao();
        kisilerDao.tweetSil(uuid).enqueue(new Callback<TweetSil>() {
            @Override
            public void onResponse(Call<TweetSil> call, Response<TweetSil> response) {
                String durum = null, mesaj = null;
                durum = response.body().getStatus();
                mesaj = response.body().getMesaj();
                Log.e("mesaj", mesaj);
                if (durum.equals("200")) {
                    loading.dismiss();
                    Toast.makeText(mContext, mesaj, Toast.LENGTH_SHORT).show();

                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, mesaj, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TweetSil> call, Throwable t) {

            }
        });

    }

    //Tarih saat ayarları yapıyoruz atılan tweetler için
    private void dateFormat(@NonNull CardTasarimTutucu holder,Tweetler tweet) {
        Date simdi = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tarih=null;
        try {
            tarih = dateFormat.parse(tweet.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int fark = (int) (simdi.getTime()-tarih.getTime());


        int gun = fark/(1000*60*60*24);
        int saat = fark/(1000*60*60);
        int dakika = fark/(1000*60);
        int saniye = fark/(1000);

        if (saniye==0)
            holder.binding.txtTarih.setText("şimdi");
        if (saniye>0 && dakika==0)
            holder.binding.txtTarih.setText(saniye+"s");
        if (dakika>0 && saat==0)
            holder.binding.txtTarih.setText(dakika+"dk");
        if (saat>0 && gun==0)
            holder.binding.txtTarih.setText(saat+"sa");
        if (gun>0)
            holder.binding.txtTarih.setText(gun+"gün");
    }

    @Override
    public int getItemCount() {
        if (tweetlerList==null){
            return 0;
        }else
        return tweetlerList.size();
    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder {
        private TweetCardTasarimBinding binding;

        public CardTasarimTutucu(@NonNull TweetCardTasarimBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
