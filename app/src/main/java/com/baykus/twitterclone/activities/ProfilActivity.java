package com.baykus.twitterclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baykus.twitterclone.R;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.databinding.ActivityProfilBinding;
import com.baykus.twitterclone.pojo.ProfilBilgileri;
import com.baykus.twitterclone.pojo.ProfilFoto;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity {
    private String id;
    private Bitmap bitmap;
    private String avatar;
    private static final String url_profil_guncelle = "http://127.0.0.1/twitterclone/profilFotoYukle.php";
    private KisilerDao kisilerDao;
    // private RequestQueue requestQueue;

    private ProgressDialog loading;
    ActivityProfilBinding profilBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilBinding = ActivityProfilBinding.inflate(getLayoutInflater());
        View view = profilBinding.getRoot();
        setContentView(view);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = preferences.getString("id", "-1");
        kisilerDao = ApiUtils.getKisilerDao();
        // requestQueue = Volley.newRequestQueue(getApplicationContext());

        Log.e("id", id);

        getProfilBilgileri();
        profilBinding.toolbarProfil.setTitle("");
        setSupportActionBar(profilBinding.toolbarProfil);
        profilBinding.imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilActivity.this, TwitterNavDrawerActivity.class));
            }
        });


        profilBinding.btnProfilDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profilBinding.imageViewProfilGuncelle.setVisibility(View.VISIBLE);
                profilBinding.imageViewProfilResimGuncelle.setVisibility(View.VISIBLE);
                profilBinding.imgback.setVisibility(View.GONE);


                profilBinding.imageViewProfilResimGuncelle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        resimSecmeIstegi();
                    }
                });
                profilBinding.imageViewProfilGuncelle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resimSecmeIstegi();

                    }
                });
                profilBinding.txtKaydet.setVisibility(View.VISIBLE);
                profilBinding.txtVazgec.setVisibility(View.VISIBLE);
                profilBinding.txtKaydet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (profilBinding.profileImage2 != null) {
                            // profilGuncellemeIstegiGonderVolley();
                            profilBinding.imageViewProfilGuncelle.setVisibility(View.GONE);
                            profilBinding.imageViewProfilResimGuncelle.setVisibility(View.GONE);
                            profilResmiGuncellemeIstegiGonderRetrofit();


                        } else {
                            Snackbar.make(profilBinding.profileImage2, "Lütfen bir resim seçiniz...", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                profilBinding.txtVazgec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profilBinding.txtKaydet.setVisibility(View.GONE);
                        profilBinding.txtVazgec.setVisibility(View.GONE);
                        profilBinding.imageViewProfilGuncelle.setVisibility(View.GONE);
                        profilBinding.imageViewProfilResimGuncelle.setVisibility(View.GONE);
                        profilBinding.imgback.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

        profilBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Bir işlem yapılıyormuş gibi 2 saniye bekletiyoruz
                new CountDownTimer(2000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        profilBinding.swipeRefresh.setRefreshing(false);
                        getProfilBilgileri();
                    }

                }.start();


            }
        });
        profilBinding.swipeRefresh.setColorSchemeResources(R.color.purple_500);


    }

    private void profilResmiGuncellemeIstegiGonderRetrofit() {

        String avatar = getStringImage(bitmap);

        Log.e("profil_image_2 : ", avatar);
        Log.e("id ", id);
        loading = ProgressDialog.show(ProfilActivity.this, "Profil güncelleniyor...",
                "Lütfen bekleyiniz...", false, true);
        kisilerDao.profilFoto(id, avatar).enqueue(new Callback<ProfilFoto>() {
            @Override
            public void onResponse(Call<ProfilFoto> call, retrofit2.Response<ProfilFoto> response) {

                String durum = response.body().getStatus();
                String mesaj = response.body().getMesaj();
                loading.dismiss();
                Log.e("Json verisi", durum);

                if (durum.equals("200")) {

                   // Snackbar.make(profilBinding.profileImage2, mesaj, Snackbar.LENGTH_LONG).show();

                    SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(ProfilActivity.this);
                    SharedPreferences.Editor editor = p.edit();
                    editor.putBoolean("ProfilChanged", true);
                    editor.commit();
                    loading.dismiss();
                    Toast.makeText(ProfilActivity.this, mesaj, Toast.LENGTH_LONG).show();
                    profilBinding.txtKaydet.setVisibility(View.GONE);
                    profilBinding.txtVazgec.setVisibility(View.GONE);
                    profilBinding.imgback.setVisibility(View.VISIBLE);

                } else {
                    //  preferences.edit().putString("id", id).commit();
                    loading.dismiss();
                    Snackbar.make(profilBinding.profileImage2, mesaj, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilFoto> call, Throwable t) {

            }
        });
    }

    private void getProfilBilgileri() {

        kisilerDao.profilBilgileri(id).enqueue(new Callback<ProfilBilgileri>() {
            @Override
            public void onResponse(Call<ProfilBilgileri> call, Response<ProfilBilgileri> response) {

                Log.e("profil bilgileri",response.body().toString());
                String durum = response.body().getStatus();
                String mesaj = response.body().getMesaj();
                String mail = response.body().getMail();
                avatar = response.body().getAvatar();

                String adsoyad = response.body().getAdsoyad();

                if (durum.equals("200")) {
                    try {
                        profilBinding.profileImage2.setImageBitmap(null);
                        Picasso.get().load(response.body().getAvatar())
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(profilBinding.profileImage2);
                    } catch (IllegalArgumentException exception) {
                        Toast.makeText(ProfilActivity.this, "Lütfen profil fotoğrafı belirleyiniz.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ProfilActivity.this, mesaj, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilBilgileri> call, Throwable t) {

            }
        });
    }


    //    private void profilGuncellemeIstegiGonderVolley() {
//
//        final ProgressDialog loading = ProgressDialog.show(ProfilActivity.this, "Profil güncelleniyor...",
//                "Lütfen bekleyiniz...", false, false);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_profil_guncelle, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                loading.dismiss();
//                Log.d("Json verisi: ", response);
//
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String durum = jsonObject.getString("status");
//                    String mesaj = jsonObject.getString("mesaj");
//                    if (durum.equals("200")) {
//                        Snackbar.make(findViewById(R.id.relativeLayout), mesaj, Snackbar.LENGTH_LONG).show();
//                        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(ProfilActivity.this);
//                        SharedPreferences.Editor editor = p.edit();
//                        editor.putBoolean("ProfilChanged", true);
//                        editor.commit();
//                    } else {
//                        Snackbar.make(findViewById(R.id.relativeLayout), mesaj, Snackbar.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                //Bitmap dan String e dönüştürülüyor
//                String image = getStringImage(bitmap);
//
//                Map<String, String> params = new HashMap<>();
//
//                //parametreler ekleniyor
//                params.put("id", id);
//                params.put("avatar", image);
//
//                return params;
//            }
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ProfilActivity.this);
//        requestQueue.add(stringRequest);
//    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void resimSecmeIstegi() {
        ActivityCompat.requestPermissions(ProfilActivity.this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Uri filePath = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                profilBinding.profileImage2.setImageBitmap(bitmap);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            profile_image_2.setImageBitmap(bitmap);
//        }
//    }

}