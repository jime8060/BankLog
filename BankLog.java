package com.example.bankingapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.bankingapp.db.BankAppDatabase;

import java.util.Date;

@Entity(tableName = BankAppDatabase.BANKLOG_TABLE)
public class BankLog {

    @PrimaryKey(autoGenerate = true)
    private int mLogId;
    private String mPurchase;
    private double mAmount;
    private int mQuantity;
    private Date mDate;
    private int mUserId;

    public BankLog(String purchase, double amount, int quantity, int userId) {
        mPurchase = purchase;
        mAmount = amount;
        mQuantity = quantity;
        mDate = new Date();
        mUserId = userId;
    }

    @Override
    public String toString() {
        return "BankLog{" +
                "mLogId=" + mLogId +
                ", mPurchase='" + mPurchase + '\'' +
                ", mAmount=" + mAmount +
                ", mQuantity=" + mQuantity +
                ", mDate=" + mDate +
                ", mUserId=" + mUserId +
                '}';
    }

    public int getLogId() {
        return mLogId;
    }

    public void setLogId(int logId) {
        mLogId = logId;
    }

    public String getPurchase() {
        return mPurchase;
    }

    public void setPurchase(String purchase) {
        mPurchase = purchase;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
