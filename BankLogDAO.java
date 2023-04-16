package com.example.bankingapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Database;

import com.example.bankingapp.BankLog;
import com.example.bankingapp.User;

import java.util.List;

@Dao
public interface BankLogDAO {
    @Insert
    void insert(BankLog... bankLogs);

    @Update
    void update(BankLog... bankLogs);

    @Delete
    void delete(BankLog bankLog);

    @Query("SELECT * FROM " + BankAppDatabase.BANKLOG_TABLE + " ORDER BY mDate DESC")
    List<BankLog> getAllBankLogs();

    @Query("SELECT * FROM " + BankAppDatabase.BANKLOG_TABLE + " WHERE mLogId = :logId ORDER BY mDate DESC")
    List<BankLog> getBankLogsById(int logId);

    @Query("SELECT * FROM " + BankAppDatabase.BANKLOG_TABLE + " WHERE mUserId = :userId ORDER BY mDate DESC")
    List<BankLog> getBankLogsByUserId(int userId);

    @Insert
    void insert(User... users);

    @Update
    void update(User...users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + BankAppDatabase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + BankAppDatabase.USER_TABLE + " WHERE mUserName = :username")
    User getUserName(String username);

    @Query("SELECT * FROM " + BankAppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);


}
