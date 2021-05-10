package com.baykus.twitterclone.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baykus.twitterclone.R;
import com.baykus.twitterclone.activities.ProfilActivity;
import com.baykus.twitterclone.api.ApiUtils;
import com.baykus.twitterclone.api.KisilerDao;
import com.baykus.twitterclone.databinding.ActivityProfilBinding;
import com.baykus.twitterclone.databinding.FragmentTweetGonderBinding;
import com.baykus.twitterclone.pojo.ProfilFoto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetGonder extends Fragment {


    private Bitmap bitmap;
    private String id;
    private Context mContext;
    private KisilerDao kisilerDao;
    private Fragment tempFragment;
    private Bundle bundle=new Bundle();
    private static final int SADECE_RESIM = 1;
    private static final int SADECE_TEXT = 2;
    private static final int TEXT_VE_RESIM = 3;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String url_tweetler = "http://10.0.2.2/TwitterClone/tweetler.php";
    FragmentTweetGonderBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTweetGonderBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //TwitterNavDrawerdan id geliyor
        id = getArguments().getString("id", "-1");
        Log.e("TweetGonderid : ", id);
        kisilerDao = ApiUtils.getKisilerDao();


        binding.txtTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                int i = 280 - s.length();
                binding.txtSayac.setText(String.valueOf(i));
                if (i < 0) {
                    binding.txtSayac.setTextColor(Color.RED);
                    binding.btnTweetle.setClickable(true);
                } else {
                    binding.txtSayac.setTextColor(Color.GRAY);
                    binding.btnTweetle.setClickable(true);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                resimSecmeIstegi();
            }
        });

        binding.txtTweetVazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tempFragment=new Anasayfa();
                bundle.putString("id",id);
                tempFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_tutucu, tempFragment).commit();

            }
        });

        binding.txtTweetVazgec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((TextView) v).setTextColor(Color.parseColor("#DD999999"));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) v).setTextColor(Color.WHITE);

                }

                return false;//tıklanma eventini etkilememesi için false bırakıyoruz...


            }
        });


        binding.btnTweetle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.txtTweet.toString().length() != 0 && bitmap != null) {
                    istekGonder(TEXT_VE_RESIM);
                }
                if (binding.txtTweet.toString().length() != 0 && bitmap == null) {
                    istekGonder(SADECE_TEXT);

                }
                if (binding.txtTweet.toString().length() == 0 && bitmap != null) {
                    istekGonder(SADECE_RESIM);
                }
                if (binding.txtTweet.toString().length() == 0 && bitmap == null) {
                    Toast.makeText(mContext, "Tweet oluşturulamadı", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    private void istekGonder(int istekTuru) {

        String uuid = UUID.randomUUID().toString();
        String text = binding.txtTweet.getText().toString();
        String resim;
        if (bitmap == null) {
            resim = binding.imageTweet.toString();
        } else {
            resim = getStringImage(bitmap);
        }


        ProgressDialog loading = ProgressDialog.show(getActivity(), "Tweet Resmi Yükleniyor",
                "Lütfen bekleyiniz...", false, true);

        kisilerDao.tweetYukle(id, uuid, String.valueOf(istekTuru), text, resim).enqueue(new Callback<ProfilFoto>() {
            @Override
            public void onResponse(Call<ProfilFoto> call, Response<ProfilFoto> response) {
                String durum = null, mesaj = null;
                durum = response.body().getStatus();
                mesaj = response.body().getMesaj();
                Log.e("mesaj", mesaj);
                if (durum.equals("200")) {
                    loading.dismiss();
                    Toast.makeText(getActivity(), mesaj, Toast.LENGTH_SHORT).show();
                    tempFragment=new Anasayfa();
                    bundle.putString("id",id);
                    tempFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_tutucu, tempFragment).commit();

                } else {
                    loading.dismiss();
                    Toast.makeText(getActivity(), mesaj, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProfilFoto> call, Throwable t) {
                loading.dismiss();

            }
        });

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.imageTweet.setImageBitmap(bitmap);
        }
    }

}