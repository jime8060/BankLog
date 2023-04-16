package com.example.bankingapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.bankingapp.db.BankAppDatabase;

@Entity(tableName = BankAppDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;
    private String mUserName;
    private String mPassword;
    private Boolean mAdmin;

    public User(String userName, String password, Boolean admin){
        mUserName = userName;
        mPassword = password;
        mAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUserId=" + mUserId +
                ", mUserName='" + mUserName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mAdmin=" + mAdmin +
                '}';
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public Boolean getAdmin() {
        return mAdmin;
    }

    public void setAdmin(Boolean admin) {
        mAdmin = admin;
    }
}
