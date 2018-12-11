package com.riza.example.articuno;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.riza.example.articuno.model.Article;
import com.riza.example.articuno.utils.DialogHelper;
import com.riza.example.articuno.utils.ENV;
import com.riza.example.articuno.utils.PrefsHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvArticle;
    TextView tvWelcome;

    private ArticleAdapter adapter;
    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvArticle = findViewById(R.id.rv_article);
        swipeRefreshLayout = findViewById(R.id.swipe);

        adapter = new ArticleAdapter();

        prefsHelper = new PrefsHelper(this);

        tvWelcome =findViewById(R.id.tv_welcome);

        tvWelcome.setText(String.format("Selamat Datang %s!", prefsHelper.getUser().getNama()));

        rvArticle.setLayoutManager(new LinearLayoutManager(this));
        rvArticle.setAdapter(adapter);

        loadData();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostActivity.class));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        adapter.setCallback(new ArticleAdapter.Callback() {
            @Override
            public void OnArticleClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.KEY_ARTICLE, adapter.getItem(position));
                startActivity(intent);
            }
        });

    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        String requestUrl= ENV.BASE_URL+"/articles";
        RequestParams params = new RequestParams();
        params.put("api_token", prefsHelper.getUser().getApiToken());

        new AsyncHttpClient().get(requestUrl,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Type listType = new TypeToken<ArrayList<Article>>() {}.getType();

                Gson gson = new GsonBuilder().create();
                ArrayList<Article> articles = gson.fromJson(response.toString(), listType);

                adapter.updateData(articles);
                swipeRefreshLayout.setRefreshing(false);
                Log.e("APIReq", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_me:

                startActivity(new Intent(this, ProfileActivity.class));

                break;
            case R.id.menu_info:

                DialogHelper.about(this).show();

                break;
        }

        return true;
    }
}
