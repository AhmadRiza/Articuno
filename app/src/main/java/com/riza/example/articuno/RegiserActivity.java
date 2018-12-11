package com.riza.example.articuno;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.riza.example.articuno.utils.DialogHelper;
import com.riza.example.articuno.utils.ENV;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegiserActivity extends AppCompatActivity {

    EditText edNama, edPassword, edPekerjaan, edTelp, edAlamat, edEmail;
    Spinner spGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Daftar");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edNama = findViewById(R.id.ed_nama);
        edPassword= findViewById(R.id.ed_password);
        edPekerjaan = findViewById(R.id.ed_pekerjaan);
        edTelp = findViewById(R.id.ed_telp);
        edAlamat = findViewById(R.id.ed_alamat);
        edEmail = findViewById(R.id.ed_email);
        spGender = findViewById(R.id.sp_gender);

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        final AlertDialog loading = DialogHelper.Loading(this);
        loading.show();

        String requestUrl = ENV.BASE_URL+"/auth/register";

        RequestParams params = new RequestParams();
        params.put("email",edEmail.getText().toString().trim());
        params.put("nama",edNama.getText().toString().trim());
        params.put("alamat",edAlamat.getText().toString().trim());
        params.put("no_telp",edTelp.getText().toString().trim());
        params.put("jenis_kelamin",spGender.getSelectedItem().toString());
        params.put("password",edPassword.getText().toString());
        params.put("pekerjaan",edPekerjaan.getText().toString());

        new AsyncHttpClient().post(requestUrl,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                boolean success = false;
                try {
                    success = response.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(success){
                    Toast.makeText(RegiserActivity.this, "Berhasil Daftar", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegiserActivity.this, LoginActivity.class);
                    intent.putExtra("email", edEmail.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegiserActivity.this, "Gagal mendaftar", Toast.LENGTH_SHORT).show();
                }
                loading.cancel();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(RegiserActivity.this, "Gagal mendaftar", Toast.LENGTH_SHORT).show();
                loading.cancel();
            }
        });

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
