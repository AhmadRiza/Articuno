package com.riza.example.articuno.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @Expose
    private String id;
    @Expose
    private String nama;
    @Expose
    private String email;
    @Expose
    private String password;

    @Expose
    private String alamat;

    @SerializedName("no_telp")
    @Expose
    private String noTelp;

    @SerializedName("jenis_kelamin")
    @Expose
    private String gender;

    @SerializedName("api_token")
    @Expose
    private String apiToken;

    @Expose
    private String pekerjaan;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nama);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.alamat);
        dest.writeString(this.noTelp);
        dest.writeString(this.gender);
        dest.writeString(this.apiToken);
        dest.writeString(this.pekerjaan);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.nama = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.alamat = in.readString();
        this.noTelp = in.readString();
        this.gender = in.readString();
        this.apiToken = in.readString();
        this.pekerjaan = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
