package com.riza.example.articuno;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.riza.example.articuno.model.User;
import com.riza.example.articuno.utils.DialogHelper;
import com.riza.example.articuno.utils.ENV;
import com.riza.example.articuno.utils.PrefsHelper;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    private PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Saya");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefsHelper = new PrefsHelper(this);
        User user = prefsHelper.getUser();

        TextView tvNama = findViewById(R.id.tv_nama);
        TextView tvEmail = findViewById(R.id.tv_email);
        TextView tvTelp = findViewById(R.id.tv_telp);
        TextView tvGender = findViewById(R.id.tv_gender);
        TextView tvAlamat = findViewById(R.id.tv_alamat);
        TextView tvKerja = findViewById(R.id.tv_kerja);

        if (user.getApiToken() != null) {
            tvNama.setText(user.getNama());
            tvEmail.setText(user.getEmail());
            tvTelp.setText(user.getNoTelp());
            tvGender.setText(user.getGender());
            tvAlamat.setText(user.getAlamat());
            tvKerja.setText(user.getPekerjaan());

        } else {
            finish();
        }

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

    }

    private void logout() {
        prefsHelper.logout();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
