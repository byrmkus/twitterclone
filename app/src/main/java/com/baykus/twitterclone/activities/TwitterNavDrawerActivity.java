package com.baykus.twitterclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baykus.twitterclone.R;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.fragments.Anasayfa;
import com.baykus.twitterclone.fragments.Bildirimler;
import com.baykus.twitterclone.fragments.Mesajlar;
import com.baykus.twitterclone.fragments.Search;
import com.baykus.twitterclone.fragments.TweetGonder;
import com.baykus.twitterclone.pojo.ProfilBilgileri;
import com.baykus.twitterclone.pojo.ProfilFoto;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwitterNavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,Serializable{

    static final float END_SCALE= 0.7f;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BottomNavigationView bottomNavigation;
    private Fragment tempFragment;
    private FragmentManager fragmentManager;
    private FloatingActionButton fabtweet;
    private TextView txtProfilUserName, txtmail;
    private CircleImageView profile_image;
    private String id;
    private CoordinatorLayout contentView;
    private KisilerDao kisilerDao;
    private SharedPreferences preferences;
    Bundle bundle = new Bundle();
    private ImageView menuIcon,twitterLogo;
    public EditText editSearch;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_twitter_nav_drawer);
        fabtweet = findViewById(R.id.fabtweet);
        navigationView = findViewById(R.id.navigatiomView);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        kisilerDao = ApiUtils.getKisilerDao();
        menuIcon = findViewById(R.id.menu_icon);
        twitterLogo = findViewById(R.id.twitter_logo);
        editSearch = findViewById(R.id.editSearch);
        contentView = findViewById(R.id.coordinatorLayout);


        //giriş ekranından gelip gelmediğini kontrol ediyoruz
        boolean girisekranindangeldim = getIntent().getBooleanExtra("animasyon", false);
        if (girisekranindangeldim) {
            //--------------giriş animasyonumuzu oluşturuyoruz--------------------------
            final LinearLayout view = new LinearLayout(TwitterNavDrawerActivity.this);
            ImageView icon = new ImageView(TwitterNavDrawerActivity.this);
            view.setGravity(Gravity.CENTER);
            view.setBackgroundColor(getResources().getColor(R.color.purple_700));
            icon.setImageResource(R.drawable.acilis_iconu);

            //iconun genişlik ve yükseklik özelliğini  250 olarak veriyoruz ve view nesnesine ekliyoruz
            view.addView(icon, 250, 250);
            getWindow().addContentView(view, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));


            Animation scaleAnim = AnimationUtils.loadAnimation(TwitterNavDrawerActivity.this, R.anim.giris_animasyonu);
            icon.clearAnimation();
            icon.startAnimation(scaleAnim);

            scaleAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
                    animator.setDuration(300);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.start();
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
//animasyon bittikten sonra görünürlük özelliği GONE olacak yani hiç orada yokmuş gibi olacak
                            view.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            //-----------Animasyon işlemlerinin sonu-----------------

        }
        // requestQueue= Volley.newRequestQueue(getApplicationContext());


        preferences = PreferenceManager.getDefaultSharedPreferences(TwitterNavDrawerActivity.this);
        id = preferences.getString("id", "-1");

        //Toolbar

        setSupportActionBar(toolbar);
        toolbar.setTitle(null);

        getProfilBilgileriRetrofit(id);

        //---------Navigation Drawer----------------


        navigationDrawer();


        //ilk açılışta Ansayfa fragment açılıyor ve giriş yapan kişinin id numarasını gönderiyoruz...

        tempFragment = new Anasayfa();
        bundle.putString("id", id);
        tempFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu, tempFragment).commit();
        twitterLogo.setVisibility(View.VISIBLE);


//Tweet atma
        fabtweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempFragment = new TweetGonder();
                bundle.putString("id", id);
                tempFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu, tempFragment).commit();

            }
        });

//Fragmentlara ulaşma için
        bottomNavigation.setBackground(null);
        bottomNavigation.getMenu().getItem(2).setEnabled(false);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navbottom_anasayfa) {
                    tempFragment = new Anasayfa();
                    bundle.putString("id", id);
                    tempFragment.setArguments(bundle);
                    toolbar.setTitle(null);
                    toolbar.setLogo(null);
                    twitterLogo.setVisibility(View.VISIBLE);
                    editSearch.setVisibility(View.INVISIBLE);
                }
                if (item.getItemId() == R.id.navbottom_mesaj) {
                    toolbar.setLogo(null);
                    toolbar.setTitle("MESAJLAR");
                    tempFragment = new Mesajlar();
                    twitterLogo.setVisibility(View.VISIBLE);
                    editSearch.setVisibility(View.INVISIBLE);
                }
                if (item.getItemId() == R.id.navbottom_bildirim) {
                    toolbar.setLogo(null);
                    toolbar.setTitle("BİLDİRİMLER");
                    tempFragment = new Bildirimler();
                    twitterLogo.setVisibility(View.VISIBLE);
                    editSearch.setVisibility(View.INVISIBLE);
                }
                if (item.getItemId() == R.id.navbottom_search) {
                    toolbar.setLogo(null);
                    toolbar.setTitle("ARAMA");
                    tempFragment = new Search();
                    bundle.putString("id", id);
                    tempFragment.setArguments(bundle);
                    twitterLogo.setVisibility(View.INVISIBLE);
                    editSearch.setVisibility(View.VISIBLE);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu, tempFragment).commit();
                return true;
            }
        });

        //Kullanıcı Arama işlermleri


        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempFragment=new Search();
                bundle.putString("search_text",s.toString().toLowerCase());
                tempFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu, tempFragment).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    //---------Navigation Drawer Functions-------

    private void navigationDrawer() {
        View baslik = navigationView.inflateHeaderView(R.layout.navigation_baslik);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.drawer);

        ConstraintLayout constraintLayout = (ConstraintLayout) navigationView.getHeaderView(0);
        txtProfilUserName = constraintLayout.findViewById(R.id.txtProfilUserName);
        txtmail = constraintLayout.findViewById(R.id.txtmail);
        profile_image = constraintLayout.findViewById(R.id.profile_image);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else drawerLayout.openDrawer(GravityCompat.START);


            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Scale the View based on current slide offset
                float diffScaledOffset = slideOffset * (1 - END_SCALE);
                float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //Translate the View, accounting for the scaled width
                float xOffset = drawerView.getWidth() * slideOffset;
                float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }

        });
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
            super.onBackPressed();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.profile_menu) {
            //profil activty e geçiş
            startActivity(new Intent(TwitterNavDrawerActivity.this, ProfilActivity.class));

        } else if (item.getItemId() == R.id.nav_gecemodu) {


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


    private void getProfilBilgileriRetrofit(String id) {

        kisilerDao.profilBilgileri(id).enqueue(new Callback<ProfilBilgileri>() {
            @Override
            public void onResponse(Call<ProfilBilgileri> call, Response<ProfilBilgileri> response) {
                String durum = response.body().getStatus();
                String mesaj = response.body().getMesaj();
                String mail = response.body().getMail();
                String avatar = response.body().getAvatar();
                String adsoyad = response.body().getAdsoyad();

                if (durum.equals("200")) {

                    Log.e("mail", mail);
                    Log.e("avatar", avatar);
                    Log.e("adsoyad", adsoyad);
                    Log.e("mesaj", mesaj);
                    txtmail.setText(response.body().getMail());
                    txtProfilUserName.setText(response.body().getAdsoyad());
                    try {

                        Picasso.get().load(response.body().getAvatar())
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(profile_image);
                    } catch (IllegalArgumentException exception) {
                        Toast.makeText(TwitterNavDrawerActivity.this, "Lütfen profil fotoğrafı belirleyiniz.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(TwitterNavDrawerActivity.this, mesaj, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilBilgileri> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getBoolean("ProfilChanged", false)) {
            getProfilBilgileriRetrofit(id);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("ProfilChanged", false);
            editor.commit();

        }
    }

    public void switchClick(View v){
        SwitchCompat switchCompat= findViewById(R.id.switchCompat);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(TwitterNavDrawerActivity.this, "Gece modu açıldı", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TwitterNavDrawerActivity.this, "Gece modu kapatıldı", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}


//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Bir işlem yapılıyormuş gibi 2 saniye bekletiyoruz
//                new CountDownTimer(2000,1000){
//
//                    @Override
//                    public void onTick(long millisUntilFinished) {}
//
//                    @Override
//                    public void onFinish() {
//                        swipeRefreshLayout.setRefreshing(false);
//                        getProfilBilgileriRetrofit(id);
//                    }
//
//                }.start();
//            }
//        });