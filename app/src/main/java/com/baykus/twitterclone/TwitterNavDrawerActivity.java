package com.baykus.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baykus.twitterclone.fragments.Anasayfa;
import com.baykus.twitterclone.fragments.Bildirimler;
import com.baykus.twitterclone.fragments.Mesajlar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TwitterNavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigation;
    private Fragment tempFragment;
    private FloatingActionButton fabtwitt;
    private static final String url_profil_bilgileri = "http://127.0.0.1/twitterclone/profilBilgileri.php";
    private TextView txtProfilUserName, txtmail;
    private CircleImageView profile_image;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_nav_drawer);
        fabtwitt = findViewById(R.id.fabtwitt);
        navigationView = findViewById(R.id.navigatiomView);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer);
        txtProfilUserName = findViewById(R.id.txtProfilUserName);
        txtmail = findViewById(R.id.txtmail);
        profile_image = findViewById(R.id.profile_image);
        toolbar = findViewById(R.id.toolbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TwitterNavDrawerActivity.this);
        id = preferences.getString("id", "-1");
        setProfilBilgileri(id);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0
                , 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View baslik = navigationView.inflateHeaderView(R.layout.navigation_baslik);

        navigationView.setNavigationItemSelectedListener(this);
        ConstraintLayout constraintLayout = (ConstraintLayout) navigationView.getHeaderView(0);
        TextView txtUserName = constraintLayout.findViewById(R.id.txtProfilUserName);
        TextView txtmail = constraintLayout.findViewById(R.id.txtmail);
        CircleImageView profilfoto = constraintLayout.findViewById(R.id.profile_image);


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutucu, new Anasayfa()).commit();

        bottomNavigation.setBackground(null);
        bottomNavigation.getMenu().getItem(2).setEnabled(false);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navbottom_anasayfa) {
                    tempFragment = new Anasayfa();
                    toolbar.setTitle(null);
                    toolbar.setLogo(R.drawable.twitter);
                }
                if (item.getItemId() == R.id.navbottom_mesaj) {
                    toolbar.setLogo(null);
                    toolbar.setTitle("MESAJLAR");
                    tempFragment = new Mesajlar();

                }
                if (item.getItemId() == R.id.navbottom_bildirim) {
                    toolbar.setLogo(null);
                    toolbar.setTitle("BİLDİRİMLER");
                    tempFragment = new Bildirimler();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu, tempFragment).commit();
                return true;
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile_menu) {
            //profil activty e geçiş
            startActivity(new Intent(TwitterNavDrawerActivity.this, ProfilActivity.class));

        } else if (item.getItemId() == R.id.nav_ikinci) {


        } else if (item.getItemId() == R.id.nav_cikis) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TwitterNavDrawerActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("benihatirla", false);
            editor.putString("id", "-1");
            editor.commit();
            startActivity(new Intent(TwitterNavDrawerActivity.this, GirisEkrani.class));
            finish();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }


    private void setProfilBilgileri(String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_profil_bilgileri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json Verisi =", response);
                String durum = "", mesaj = "", adsoyad = "", avatar = "", mail = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    durum = jsonObject.getString("status");
                    mesaj = jsonObject.getString("mesaj");
                    avatar = jsonObject.getString("avatar");
                    adsoyad = jsonObject.getString("adsoyad");
                    mail = jsonObject.getString("mail");
                } catch (JSONException e) {
                    Log.e("Json parse hatası :", e.getLocalizedMessage());
                }
                if (durum.equals("200")) {
                    txtmail.setText(mail);
                    txtProfilUserName.setText(adsoyad);
                    Picasso.get()
                            .load(avatar)
                            .resize(70, 70)
                            .centerCrop()
                            .into(profile_image);


                } else {
                    Toast.makeText(TwitterNavDrawerActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> degerler = new HashMap<>();
                degerler.put("id", id);
                return degerler;
            }
        };
    }


}