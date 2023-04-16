package com.example.bankingapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bankingapp.BankLog;
import com.example.bankingapp.User;
import com.example.bankingapp.db.TypeConverter.DateTypeConverter;

@Database(entities = {BankLog.class, User.class}, version = 1)
@TypeConverters(DateTypeConverter.class)
public abstract class BankAppDatabase extends RoomDatabase {
    public static final String DB_NAME = "BANKLOG_DATABASE";
    public static final String BANKLOG_TABLE = "BANKLOG_TABLE";
    public static final String USER_TABLE = "USER_TABLE";

    public abstract BankLogDAO getBankLogDAO();
}
