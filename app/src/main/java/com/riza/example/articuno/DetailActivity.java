package com.riza.example.articuno;

import android.content.Context;
import android.content.DialogInterface;
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
import com.riza.example.articuno.model.Article;
import com.riza.example.articuno.utils.DialogHelper;
import com.riza.example.articuno.utils.ENV;
import com.riza.example.articuno.utils.PrefsHelper;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_ARTICLE ="article";

    Article article;
    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        prefsHelper = new PrefsHelper(this);

        article = getIntent().getParcelableExtra(KEY_ARTICLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(article.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(article.getTitle());

        TextView tvAuthor = findViewById(R.id.tv_author);
        tvAuthor.setText(article.getAuthor());

        TextView tvCategories = findViewById(R.id.tv_categories);
        tvCategories.setText(String.format("#%s", article.getCategories()));

        TextView tvContent = findViewById(R.id.tv_content);
        tvContent.setText(article.getContent());

        if(prefsHelper.getUser().getId().equals(article.getUserId())){
            findViewById(R.id.layout_edit).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailActivity.this, PostActivity.class);
                    intent.putExtra(KEY_ARTICLE, article);
                    startActivity(intent);
                    finish();
                }
            });
            findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogHelper.confirm(DetailActivity.this, "Anda yakin menghapus " + article.getTitle(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            delete();
                        }
                    }).show();

                }
            });

        }


    }

    private void delete() {

        final AlertDialog loading = DialogHelper.Loading(this);
        loading.show();

        String reqUrl = ENV.BASE_URL+"/articles/delete/"
                +article.getId()+"?api_token="
                +prefsHelper.getUser().getApiToken();

        new AsyncHttpClient().delete(reqUrl, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(DetailActivity.this, "Berhasil menghapus", Toast.LENGTH_SHORT).show();
                loading.cancel();
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(DetailActivity.this, "Gagal menghapus", Toast.LENGTH_SHORT).show();
                loading.cancel();
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
