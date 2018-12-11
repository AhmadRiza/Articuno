package com.riza.example.articuno;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.riza.example.articuno.model.Article;
import com.riza.example.articuno.utils.DialogHelper;
import com.riza.example.articuno.utils.ENV;
import com.riza.example.articuno.utils.PrefsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PostActivity extends AppCompatActivity {


    boolean isUpdate = false;
    EditText edTitle, edContent, edStatus;
    Spinner spCategories;

    PrefsHelper prefsHelper;

    List<String > categories = new ArrayList<>();
    ArrayAdapter<String > adapter;

    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Buat Artikel Baru");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefsHelper = new PrefsHelper(this);

        edTitle = findViewById(R.id.ed_title);
        edContent = findViewById(R.id.ed_content);
        edStatus = findViewById(R.id.ed_status);
        spCategories = findViewById(R.id.sp_category);
        adapter = new ArrayAdapter<>(this, R.layout.row_spinner, categories);
        spCategories.setAdapter(adapter);


        upDateCategoryList();

        article = getIntent().getParcelableExtra(DetailActivity.KEY_ARTICLE);

        if (article != null) {
            edTitle.setText(article.getTitle());
            edContent.setText(article.getContent());
            edStatus.setText(article.getStatus());
            getSupportActionBar().setTitle("Edit "+article.getTitle());
            isUpdate = true;
        }

        findViewById(R.id.btn_add_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogHelper.addCategory(PostActivity.this, new DialogHelper.CategoryAddCallback() {
                    @Override
                    public void onClickOk(String category) {
                        addCategory(category);
                    }
                }).show();

            }
        });

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isUpdate) {
                    upDate(article.getId());
                } else {
                    insert();
                }

            }
        });

    }

    private void insert() {

        final AlertDialog loading = DialogHelper.Loading(this);
        loading.show();

        String reqUrl = ENV.BASE_URL+"/articles/store?api_token="+prefsHelper.getUser().getApiToken();
        RequestParams params = new RequestParams();
        params.put("user_id",prefsHelper.getUser().getId());

        String category = spCategories.getSelectedItem().toString();
        String kategoriId = category.split("\\s+")[0];

        params.put("user_id",prefsHelper.getUser().getId());
        params.put("kategori_id",kategoriId);
        params.put("content",edContent.getText().toString());
        params.put("status",edStatus.getText().toString());
        params.put("judul",edTitle.getText().toString());

        new AsyncHttpClient().post(reqUrl, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(PostActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                loading.cancel();
                escapetoHome();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(PostActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                loading.cancel();
            }
        });


    }

    private void upDate(String id) {

        final AlertDialog loading = DialogHelper.Loading(this);
        loading.show();


        String reqUrl = ENV.BASE_URL+"/articles/update/"+id+"?api_token="+prefsHelper.getUser().getApiToken();
        RequestParams params = new RequestParams();
        params.put("user_id",prefsHelper.getUser().getId());

        String category = spCategories.getSelectedItem().toString();
        String kategoriId = category.split("\\s+")[0];

        params.put("kategori_id",kategoriId);
        params.put("content",edContent.getText().toString());
        params.put("status",edStatus.getText().toString());
        params.put("judul",edTitle.getText().toString());

        new AsyncHttpClient().put(reqUrl, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(PostActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                loading.cancel();
                escapetoHome();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(PostActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                loading.cancel();
            }
        });

    }


    private void upDateCategoryList() {

        final AlertDialog loading = DialogHelper.Loading(this);
        loading.show();

        String reqUrl = ENV.BASE_URL + "/categories?api_token=" + prefsHelper.getUser().getApiToken();

        new AsyncHttpClient().get(reqUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                categories.clear();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        categories.add(jsonObject.getInt("id") + " " + jsonObject.getString("kategori"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loading.cancel();

                adapter.notifyDataSetChanged();

                if(isUpdate){

                    for (int i = 0; i < spCategories.getCount(); i++) {
                        String cat = spCategories.getItemAtPosition(i).toString().split("\\s+")[1];
                        if (cat.trim().equalsIgnoreCase(article.getCategories())) {
                            spCategories.setSelection(i);
                            break;
                        }
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                loading.cancel();
            }
        });


    }

    private void addCategory(String category){

        final AlertDialog loading = DialogHelper.Loading(this);
        loading.show();

        String reqUrl = ENV.BASE_URL +"/categories/store?api_token="+prefsHelper.getUser().getApiToken();
        RequestParams params = new RequestParams();
        params.put("kategori", category);
        new AsyncHttpClient().post(reqUrl,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(PostActivity.this, "Kategori ditambahkan", Toast.LENGTH_SHORT).show();
                loading.cancel();
                upDateCategoryList();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(PostActivity.this, "Gagal menambah", Toast.LENGTH_SHORT).show();
                loading.cancel();
            }

        });

    }


    private void escapetoHome(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
