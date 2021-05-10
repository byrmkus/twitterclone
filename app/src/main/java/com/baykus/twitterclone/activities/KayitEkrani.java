package com.baykus.twitterclone.activities;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baykus.twitterclone.R;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.pojo.Register;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class KayitEkrani extends AppCompatActivity {
    private ImageView imageKayit;
    private FloatingActionButton fabKayit;
    private TextInputLayout tilAdSoyad, tilMailKayit, tilKulaniciAdiKayit, tilSifreKayit;
    private TextInputEditText adSoyad, mailKayit, kulaniciAdiKayit, sifreKayit;
    private RequestQueue requestQueue;
    private static final String url_kayit = "http://busrakus.cf/twitterclone/register.php";
    private KisilerDao kisilerDao;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ekrani);
        imageKayit = findViewById(R.id.imageKayit);
        fabKayit = findViewById(R.id.fabGiris);
        tilAdSoyad = findViewById(R.id.tilAdSoyad);
        tilMailKayit = findViewById(R.id.tilMailKayit);
        tilKulaniciAdiKayit = findViewById(R.id.tilKulaniciAdiKayit);
        tilSifreKayit = findViewById(R.id.tilSifreKayit);
        adSoyad = findViewById(R.id.adSoyad);
        mailKayit = findViewById(R.id.mailKayit);
        kulaniciAdiKayit = findViewById(R.id.kulaniciAdiKayit);
        sifreKayit = findViewById(R.id.sifreKayit);


        kisilerDao = ApiUtils.getKisilerDao();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //   requestQueue = Volley.newRequestQueue(getApplicationContext());
//internet bağlantısı kontrolu yapıyoruz..
        if (!InternetBaglantiKontrol()) {
            Snackbar.make(findViewById(R.id.frameLayoutKayitEkrani), "Lütfen internet baglantisini kontrol ediniz...",
                    Snackbar.LENGTH_LONG).show();
        }


        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager wm = window.getWindowManager();
        Display ekran = wm.getDefaultDisplay();

        Point point = new Point();

        ekran.getSize(point);

        int genislik = point.x;
        int yukseklik = point.y;
        //1920x1080 en boy oranı 1.78
        //Arka plandaki resimi kaydırıyor ve resmin tüm ekranı kaplamasını sağlıyor
        imageKayit.getLayoutParams().width = (int) (yukseklik * 1.78);
        imageKayit.getLayoutParams().height = (yukseklik);

        ObjectAnimator animator = ObjectAnimator.ofFloat(imageKayit, "x", 0,
                -(yukseklik * 1.78f - genislik), 0, -(yukseklik * 1.78f - genislik));
        animator.setDuration(210000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();


        findViewById(R.id.txtZatenHesabimVar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitEkrani.this, GirisEkrani.class);
                NavUtils.navigateUpTo(KayitEkrani.this, intent);
            }
        });
        findViewById(R.id.txtZatenHesabimVar).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((TextView) v).setTextColor(Color.parseColor("#DD999999"));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) v).setTextColor(Color.WHITE);
                }
                return false;//tıklanma eventini etkilememis için false bırakıyoruz...
            }
        });


        fabKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean durumAdSoyad = TextUtils.isEmpty(adSoyad.getText());
                boolean durumMailKayit = TextUtils.isEmpty(mailKayit.getText());
                boolean durumKulaniciAdiKayit = TextUtils.isEmpty(kulaniciAdiKayit.getText());
                boolean durumSifreKayit = TextUtils.isEmpty(sifreKayit.getText());
                tilAdSoyad.setError(null);
                tilKulaniciAdiKayit.setError(null);
                tilSifreKayit.setError(null);
                tilMailKayit.setError(null);
                if (durumAdSoyad || durumMailKayit || durumKulaniciAdiKayit || durumSifreKayit ||
                        !mailKayit.getText().toString().contains("@")) {
                    if (durumAdSoyad) {
                        tilAdSoyad.setError("Lütfen ad ve soyadınızı giriniz");
                    }
                    if (durumKulaniciAdiKayit) {
                        tilKulaniciAdiKayit.setError("Lütfen kullanıcı adı giriniz");
                    }
                    if (durumSifreKayit) {
                        tilSifreKayit.setError("Lütfen geçerli bir şifre giriniz");
                    }
                    if (durumMailKayit) {
                        tilMailKayit.setError("Lütfen mail adresi giriniz");
                    } else if (!mailKayit.getText().toString().contains("@")) {
                        tilMailKayit.setError("Lütfen geçerli bir mail adresi giriniz");
                    }
                } else {
                    //kayıt işlemi yapılacak
                    if (!InternetBaglantiKontrol()) {
                        Snackbar.make(findViewById(R.id.frameLayoutKayitEkrani), "Lütfen internet baglantisini kontrol ediniz...",
                                Snackbar.LENGTH_LONG).show();
                    } else {
                        kisiEkleRetrofit();
                        //istekGonderVolley();
                    }

                }
            }
        });
    }

    private void kisiEkleRetrofit() {
        String adsoyad = adSoyad.getText().toString();
        String mail = mailKayit.getText().toString();
        String kulaniciadi = kulaniciAdiKayit.getText().toString();
        String sifre = sifreKayit.getText().toString();

        kisilerDao.kisiEkle(kulaniciadi, sifre, mail, adsoyad).enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, retrofit2.Response<Register> response) {

                String status = null, mesaj = null;
               status = response.body().getStatus();
                mesaj = response.body().getMesaj();

                Log.e("Json verisi", response.body().toString());
                Log.e("Durum", status);
                if (status.equals("200")) {
//  preferences.edit().putString("id", id).commit();
                    Toast.makeText(KayitEkrani.this, mesaj, Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(KayitEkrani.this)
                            .setMessage("Merhaba "+response.body().getAdsoyad()+" Kayıt işlemi başarılı bir şekilde yapıldı. Lütfen mail adresinizi kontrol ediniz.")
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();


                } else if (status.equals("400")) {
                    Snackbar.make(findViewById(R.id.frameLayoutKayitEkrani),
                            "Verilen bilgiler ile kayit yapılamadı.Kullanıcı adı daha önce kullanılmış.", Snackbar.LENGTH_LONG).show();

                } else if (status.equals("404")) {
                    Snackbar.make(findViewById(R.id.frameLayoutKayitEkrani),
                            "Sunucu ile baglanti kurulamadi...", Snackbar.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {

            }
        });

    }

//    private void istekGonderVolley() {
//
//        StringRequest request = new StringRequest(Request.Method.POST, url_kayit, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("Json verisi", response);
//
//                try {
//                    JSONObject json = new JSONObject(response);
//                    String id = json.getString("id");
//                    String durum = json.getString("status");
//
//                    if (durum.equals("404")) {
//
//                        Snackbar.make(findViewById(R.id.frameLayoutKayitEkrani),
//                                "Sunucu ile baglanti kurulamadi...", Snackbar.LENGTH_LONG).show();
//
//                    } else if (durum.equals("400")) {
//                        Snackbar.make(findViewById(R.id.frameLayoutKayitEkrani),
//                                "Verilen bilgiler ile kayit yapılamadı.Kullanıcı adı daha önce kullanılmış.", Snackbar.LENGTH_LONG).show();
//
//                    } else if (durum.equals("200")) {
//                        //  preferences.edit().putString("id", id).commit();
//
//                        new AlertDialog.Builder(KayitEkrani.this)
//                                .setMessage("Kayıt işlemi başarılı bir şekilde yapıldı. Lütfen mail adresinizi kontrol ediniz.")
//                                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        finish();
//                                    }
//                                }).show();
//                    }
//
//                } catch (JSONException e) {
//                    Log.e("parse hatası", e.getLocalizedMessage());
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> degerler = new HashMap<>();
//                degerler.put("kullaniciadi", kulaniciAdiKayit.getText().toString());
//                degerler.put("sifre", sifreKayit.getText().toString());
//                degerler.put("adsoyad", adSoyad.getText().toString());
//                degerler.put("mail", mailKayit.getText().toString());
//
//                return degerler;
//            }
//        };
//        requestQueue.add(request);
//
//    }

    public boolean InternetBaglantiKontrol() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null
                && manager.getActiveNetworkInfo().isAvailable()
                && manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}