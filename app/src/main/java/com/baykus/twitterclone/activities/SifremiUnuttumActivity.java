package com.baykus.twitterclone.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.baykus.twitterclone.R;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.databinding.ActivitySifremiUnuttumBinding;
import com.baykus.twitterclone.pojo.Register;
import com.baykus.twitterclone.pojo.SifreSifirla;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SifremiUnuttumActivity extends AppCompatActivity {
    private KisilerDao kisilerDao;
    ActivitySifremiUnuttumBinding sifremiUnuttumBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifremi_unuttum);
        sifremiUnuttumBinding = ActivitySifremiUnuttumBinding.inflate(getLayoutInflater());
        View view = sifremiUnuttumBinding.getRoot();
        setContentView(view);

        kisilerDao= ApiUtils.getKisilerDao();


        sifremiUnuttumBinding.sifreSifirlaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sifremiUnuttumBinding.textInputLayout.setError(null);

                if (sifremiUnuttumBinding.emailsifremiunuttum.getText().toString().trim().length() == 0) {
                    sifremiUnuttumBinding.textInputLayout.setError("Lütfen mail adresinizi giriniz");
                } else if (!sifremiUnuttumBinding.emailsifremiunuttum.getText().toString().contains("@")) {
                    sifremiUnuttumBinding.textInputLayout.setError("Lütfen geçerli bir mail adresi giriniz");
                } else {
                    Log.e("durum ","else düşmüş");
                    istekGonder(sifremiUnuttumBinding.emailsifremiunuttum.getText().toString());
                }
            }
        });
    }



    private void istekGonder(String email) {
        kisilerDao.sifreSifirla(email).enqueue(new Callback<SifreSifirla>() {
            @Override
            public void onResponse(Call<SifreSifirla> call, Response<SifreSifirla> response) {
                Log.e("durum ",response.body().getStatus());
                if (response.body().getStatus().equals("200")) {
                    new AlertDialog.Builder(SifremiUnuttumActivity.this)
                            .setMessage("Şifrenizi yenilemeniz için size bir mail gönderdik. Lütfen e-postanızı kontrol ediniz.")
                            .show();

                } else {
                    Snackbar.make(findViewById(R.id.rootSifremiUnuttum), response.body().getMesaj(), Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SifreSifirla> call, Throwable t) {

            }
        });

    }
}