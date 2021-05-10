package com.baykus.twitterclone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
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
import android.widget.Button;
import android.widget.CheckBox;
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
import com.baykus.twitterclone.pojo.Login;
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

public class GirisEkrani extends AppCompatActivity {

    private ImageView image;
    private Button btnKayit;
    private FloatingActionButton fabGiris;
    private TextInputLayout  tilKulaniciAdi, tilSifre;
    private TextInputEditText kulaniciAdi, sifre;
    private CheckBox benihatirla;
    private RequestQueue requestQueue;
    private static final String url_login = "https://bayramkus.com/twitterclone/login.php";
    private KisilerDao kisilerDao;
    private SharedPreferences preferences;
    private boolean istekGonderildi = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        image = findViewById(R.id.image);
        fabGiris = findViewById(R.id.fabGiris);
        btnKayit = findViewById(R.id.btnKayit);
        tilKulaniciAdi = findViewById(R.id.tilKulaniciAdi);
        tilSifre = findViewById(R.id.tilSifre);
        kulaniciAdi = findViewById(R.id.kulaniciAdi);
        sifre = findViewById(R.id.sifre);
        benihatirla = findViewById(R.id.checkboxBeniHatırla);


        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
               // requestQueue= Volley.newRequestQueue(getApplicationContext());
        kisilerDao = ApiUtils.getKisilerDao();

//beni hatırla seçiliyse direk ana ekrana geçiş
        if (preferences.getBoolean("benihatirla", false)) {
            Intent intent=new Intent(GirisEkrani.this, TwitterNavDrawerActivity.class);
            intent.putExtra("animasyon",true);
            startActivity(intent);
            GirisEkrani.this.finish();

        }
   
//internet bağlantısı kontrolu yapıyoruz..
        if (!InternetBaglantiKontrol()) {
            Snackbar.make(findViewById(R.id.frameLayoutGirisEkrani), "Lütfen internet baglantisini kontrol ediniz...",
                    Snackbar.LENGTH_LONG).show();
        }


        findViewById(R.id.txtSifremiUnuttum).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    ((TextView)v).setTextColor(Color.parseColor("#DD999999"));
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    ((TextView)v).setTextColor(Color.WHITE);
                }

                return true;//tıklanma eventini etkilememesi için false bırakıyoruz...


            }
        });

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager wm = window.getWindowManager();
        Display ekran = wm.getDefaultDisplay();

        Point point = new Point();

        ekran.getSize(point);

        int genislik = point.x;
        int yukseklik = point.y;
        //1920x1080 en boy oranı 1.78
        image.getLayoutParams().width = (int) (yukseklik * 1.78);
        image.getLayoutParams().height = (yukseklik);

        ObjectAnimator animator = ObjectAnimator.ofFloat(image, "x", 0, -(yukseklik * 1.78f - genislik), 0, -(yukseklik * 1.78f - genislik));
        animator.setDuration(210000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();


        fabGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean durumKulaniciAdi = TextUtils.isEmpty(kulaniciAdi.getText());
                boolean durumSifre = TextUtils.isEmpty(sifre.getText());
                tilKulaniciAdi.setError(null);
                tilSifre.setError(null);

                if (durumKulaniciAdi || durumSifre) {

                    if (durumKulaniciAdi) {
                        tilKulaniciAdi.setError("Lütfen kullanıcı adı giriniz");
                    }
                    if (durumSifre) {
                        tilSifre.setError("Lütfen geçerli bir şifre giriniz");
                    }

                } else {
                    //giris işlemi yapılacak
                    if (!InternetBaglantiKontrol()) {
                        Snackbar.make(findViewById(R.id.frameLayoutGirisEkrani), "Lütfen internet baglantisini kontrol ediniz...",
                                Snackbar.LENGTH_LONG).show();
                    } else {

                        girisYapRetrofit();
                       //girisYapVolley();

                    }

                }
            }
        });


        btnKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(GirisEkrani.this,KayitEkrani.class));
            }
        });
    }

    private void girisYapVolley() {
        StringRequest request = new StringRequest(Request.Method.POST, url_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json verisi", response);
                istekGonderildi = false;

                try {
                    JSONObject json = new JSONObject(response);
                    String mesaj = json.getString("mesaj");
                    String durum = json.getString("status");

                    if (durum.equals("200")){
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("id", json.getString("id"));
                        editor.putBoolean("benihatirla", benihatirla.isChecked());
                        editor.commit();
                        //ana ekrana geçiş
                        Intent intent=new Intent(GirisEkrani.this, TwitterNavDrawerActivity.class);
                        intent.putExtra("animasyon",true);
                        startActivity(intent);
                        GirisEkrani.this.finish();
                        //ana ekrana geçiş.

                    }else {
                        Toast.makeText(GirisEkrani.this, mesaj, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> degerler = new HashMap<>();
                degerler.put("kullaniciadi", kulaniciAdi.getText().toString());
                degerler.put("sifre", sifre.getText().toString());
                return degerler;
            }
        };
        requestQueue.add(request);

    }


    private void girisYapRetrofit() {
        String kullaniciAdi = kulaniciAdi.getText().toString();
        String sifre1 = sifre.getText().toString();
        Log.e("asdf","girşi");
        kisilerDao.kisiLogin(kullaniciAdi,sifre1).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, retrofit2.Response<Login> response) {
                Log.e("asdf1","girşi1");
                Log.e("asdf1",response.body().toString());

                if (response.body().getStatus().equals("200")){

                    startActivity(new Intent(GirisEkrani.this,TwitterNavDrawerActivity.class));
                    GirisEkrani.this.finish();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id",response.body().getId());
                    editor.putBoolean("benihatirla",benihatirla.isChecked());
                    editor.commit();

                }else {
                    Toast.makeText(GirisEkrani.this,response.body().getMesaj(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });

    }


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