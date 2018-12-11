package com.riza.example.articuno;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riza.example.articuno.model.Article;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private Callback callback;
    private ArrayList<Article> data = new ArrayList<>();


    public ArticleAdapter() {
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void updateData(ArrayList<Article> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_article,viewGroup,false);
        final ArticleHolder holder = new ArticleHolder(view);
        if(callback!=null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnArticleClick(holder.getPosition());
                }
            });
        }
        return holder;
    }

    public Article getItem(int position){
        return data.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder articleHolder, int i) {
        articleHolder.tvTitle.setText(getItem(i).getTitle());
        articleHolder.tvSubtitle.setText(getItem(i).getAuthor());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ArticleHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvSubtitle;

        public ArticleHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);

        }
    }

    public interface Callback {
        void OnArticleClick(int position);
    }

}
