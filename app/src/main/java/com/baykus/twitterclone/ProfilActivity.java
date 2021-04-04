package com.baykus.twitterclone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.pojo.CRUDCevap;
import com.baykus.twitterclone.pojo.ProfilFoto;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfilActivity extends AppCompatActivity {
    private String id;
    private ImageView profilImageView, imageViewResimGuncelle, imageViewProfilGuncelle, imgback;
    private TextView txtKaydet, txtVazgec;
    private Toolbar toolbar_profil;
    private Button profilDüzenle;
    private CircleImageView profile_image_2;
    private Bitmap bitmap;
    private static final String url_profil_guncelle = "http://127.0.0.1/twitterclone/profilFotoYukle.php";
    private KisilerDao kisilerDao;
    private RequestQueue requestQueue;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        profilImageView = findViewById(R.id.imageView_profil);
        imgback = findViewById(R.id.imgback);
        toolbar_profil = findViewById(R.id.toolbar_profil);
        txtKaydet = findViewById(R.id.txtKaydet);
        txtVazgec = findViewById(R.id.txtVazgec);
        profilDüzenle = findViewById(R.id.profil_düzenle);
        profile_image_2 = findViewById(R.id.profile_image_2);
        imageViewResimGuncelle = findViewById(R.id.imageViewProfilResimGuncelle);
        imageViewProfilGuncelle = findViewById(R.id.imageViewProfilGuncelle);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = preferences.getString("id", "-1");
        kisilerDao = ApiUtils.getKisilerDao();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Log.e("id", id);

        toolbar_profil.setTitle("");
        setSupportActionBar(toolbar_profil);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilActivity.this, TwitterNavDrawerActivity.class));
            }
        });
        profilDüzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageViewProfilGuncelle.setVisibility(View.VISIBLE);
                imageViewResimGuncelle.setVisibility(View.VISIBLE);
                imgback.setVisibility(View.GONE);


                imageViewResimGuncelle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        resimSecmeIstegi();
                    }
                });
                imageViewProfilGuncelle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resimSecmeIstegi();

                    }
                });
                txtKaydet.setVisibility(View.VISIBLE);
                txtVazgec.setVisibility(View.VISIBLE);
                txtKaydet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bitmap != null) {
                            // profilGuncellemeIstegiGonderVolley();
                            imageViewProfilGuncelle.setVisibility(View.GONE);
                            imageViewResimGuncelle.setVisibility(View.GONE);
                            profilGuncellemeIstegiGonderRetrofit();

                        } else {
                            Snackbar.make(profile_image_2, "Lütfen bir resim seçiniz...", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                txtVazgec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtKaydet.setVisibility(View.GONE);
                        txtVazgec.setVisibility(View.GONE);
                        imageViewProfilGuncelle.setVisibility(View.GONE);
                        imageViewResimGuncelle.setVisibility(View.GONE);
                        imgback.setVisibility(View.VISIBLE);
                    }
                });

            }
        });


    }

    private void profilGuncellemeIstegiGonderRetrofit() {

        String avatar = getStringImage(bitmap);

        Log.e("profil_image_2 : ", avatar);
        Log.e("id ", id);
        ProgressDialog loading = ProgressDialog.show(ProfilActivity.this, "Profil güncelleniyor...",
                "Lütfen bekleyiniz...", false, false);
        kisilerDao.profilFoto(id, avatar).enqueue(new Callback<ProfilFoto>() {
            @Override
            public void onResponse(Call<ProfilFoto> call, retrofit2.Response<ProfilFoto> response) {

                String durum = response.body().getStatus();
                String mesaj = response.body().getMesaj();
                loading.dismiss();
                Log.e("Json verisi", durum);

                if (durum.equals("200")) {
                    Snackbar.make(profile_image_2, mesaj, Snackbar.LENGTH_LONG).show();

                    SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(ProfilActivity.this);
                    SharedPreferences.Editor editor = p.edit();
                    editor.putBoolean("ProfilChanged", true);
                    editor.commit();
                    loading.dismiss();
                    Toast.makeText(ProfilActivity.this, mesaj, Toast.LENGTH_LONG).show();
                    txtKaydet.setVisibility(View.GONE);
                    txtVazgec.setVisibility(View.GONE);
                    imgback.setVisibility(View.VISIBLE);


                } else {
                    //  preferences.edit().putString("id", id).commit();
                    loading.dismiss();
                    Snackbar.make(profile_image_2, mesaj, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ProfilFoto> call, Throwable t) {

            }
        });


    }

    private void profilGuncellemeIstegiGonderVolley() {

        final ProgressDialog loading = ProgressDialog.show(ProfilActivity.this, "Profil güncelleniyor...",
                "Lütfen bekleyiniz...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_profil_guncelle, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Log.d("Json verisi: ", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String durum = jsonObject.getString("status");
                    String mesaj = jsonObject.getString("mesaj");
                    if (durum.equals("200")) {
                        Snackbar.make(findViewById(R.id.relativeLayout), mesaj, Snackbar.LENGTH_LONG).show();
                        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(ProfilActivity.this);
                        SharedPreferences.Editor editor = p.edit();
                        editor.putBoolean("ProfilChanged", true);
                        editor.commit();
                    } else {
                        Snackbar.make(findViewById(R.id.relativeLayout), mesaj, Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Bitmap dan String e dönüştürülüyor
                String image = getStringImage(bitmap);

                Map<String, String> params = new HashMap<>();

                //parametreler ekleniyor
                params.put("id", id);
                params.put("avatar", image);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProfilActivity.this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void resimSecmeIstegi() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim seçiniz"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            profile_image_2.setImageBitmap(bitmap);
        }
    }

}