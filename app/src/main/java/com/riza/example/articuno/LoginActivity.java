package com.riza.example.articuno;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.riza.example.articuno.model.User;
import com.riza.example.articuno.utils.DialogHelper;
import com.riza.example.articuno.utils.ENV;
import com.riza.example.articuno.utils.PrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private PrefsHelper prefsHelper;

    EditText edEmail, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefsHelper = new PrefsHelper(this);
        verifyLogin();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Log In");
        setSupportActionBar(toolbar);

        edEmail = findViewById(R.id.ed_email);
        edPassword = findViewById(R.id.ed_password);

        String receivedEmail = getIntent().getStringExtra("email");
        if(receivedEmail!=null){
            edEmail.setText(receivedEmail);
        }

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegiserActivity.class));
            }
        });

    }

    private void login() {

        final AlertDialog dialog =  DialogHelper.Loading(this);
        dialog.show();

        String requestUrl = ENV.BASE_URL+"/auth/login";
        RequestParams params = new RequestParams();
        params.put("email", edEmail.getText().toString().trim());
        params.put("password", edPassword.getText().toString().trim());

        new AsyncHttpClient().post(requestUrl, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String userString = null;
                try {
                    userString = response.getString("user");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new GsonBuilder().create();
                User user = gson.fromJson(userString, User.class);
                prefsHelper.saveUser(user);

                verifyLogin();

                dialog.cancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });



    }

    private void verifyLogin() {
        if(prefsHelper.getUser().getApiToken()!=null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
