package com.example.bankingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankingapp.db.BankAppDatabase;
import com.example.bankingapp.db.BankLogDAO;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameEntry;
    private EditText mPasswordEntry;
    private Button mButton;
    private BankLogDAO mBankLogDAO;
    private String mUsername;
    private String mPassword;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectDisplay();
        getDatabase();

    }

    private void connectDisplay(){
        mUsernameEntry = findViewById(R.id.editTextLoginUserName);
        mPasswordEntry = findViewById(R.id.editTextLoginPassword);

        mButton = findViewById(R.id.buttonLogin);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                        startActivity(intent);
                        if(mUser.getAdmin() == true) // DELETE
                            Toast.makeText(LoginActivity.this,"Welcome Admin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button newUserButton = findViewById(R.id.newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Sorry, registration closed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }

    // add something similar for admin? boolean validate isAdmin()
    private boolean validateIsAdmin() {
        return mUser.getAdmin().equals(true);
    }

    private boolean checkForUserInDatabase(){
        mUser = mBankLogDAO.getUserName(mUsername);
        if(mUser == null){
            Toast.makeText(this,"No user " + mUsername + " found.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getDatabase(){
        mBankLogDAO = Room.databaseBuilder(this, BankAppDatabase.class, BankAppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getBankLogDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    private void getValuesFromDisplay(){
        mUsername = mUsernameEntry.getText().toString();
        mPassword = mPasswordEntry.getText().toString();
    }

}


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//    }