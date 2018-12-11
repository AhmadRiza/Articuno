package com.riza.example.articuno.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article implements Parcelable {

    @SerializedName("judul")
    @Expose
    private String title;

    private String id;

    private String content;

    @SerializedName("nama")
    @Expose
    private String author;

    @SerializedName("kategori")
    @Expose
    private String categories;

    @SerializedName("user_id")
    @Expose
    private String userId;

    private String status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.id);
        dest.writeString(this.content);
        dest.writeString(this.author);
        dest.writeString(this.categories);
        dest.writeString(this.userId);
        dest.writeString(this.status);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.title = in.readString();
        this.id = in.readString();
        this.content = in.readString();
        this.author = in.readString();
        this.categories = in.readString();
        this.userId = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
