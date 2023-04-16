/*
Miguel Jimenez
Dr. C
CST-338 Project 2
4/15/2023
Used GymLog app as a base
 */

package com.example.bankingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.example.bankingapp.db.BankAppDatabase;
import com.example.bankingapp.db.BankLogDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "userIdKey";
    private static final String PREFERENCES_KEY = "Key";
    private TextView mMainDisplay;
    private TextView mScroll;
    private EditText mPurchase;
    private EditText mAmount;
    private EditText mQuantity;
    private Button mMainSubmitButton;
    private Button mAdminButton;
    private BankLogDAO mBankLogDAO;
    private List<BankLog> mBankLogs;
    private int mUserId = -1;
    private SharedPreferences mPreferences = null;
    private User mUser;
    private Button mNewAcct;

    // setters and getters
    public Button getNewAcct() {return mNewAcct;}
    public void setNewAcct(Button newAcct) {
        mNewAcct = newAcct;
    }
    public Button getAdminButton() {return mAdminButton;}
    public void setAdminButton(Button adminButton) {mAdminButton = adminButton;}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();
        checkForUser();
        loginUser(mUserId);

        mMainDisplay = findViewById(R.id.mainBankingLogDisplay);
        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());

        mPurchase = findViewById(R.id.mainPurchaseEditText);
        mAmount = findViewById(R.id.mainAmountEditText);
        mQuantity = findViewById(R.id.mainQuantityEditText);

        mMainSubmitButton = findViewById(R.id.mainSubmitButton);

        refreshDisplay();

        // hit submit button and save entries to log
        mMainSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankLog log = getValuesFromDisplay();
                mBankLogDAO.insert(log);
                refreshDisplay();
            }
        });

        // open LoanPage button
        Button loanPageButton = findViewById(R.id.loanPageButton);
        loanPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoanCalc();
            }
        });

        // open Admin button
        View b = findViewById(R.id.adminButton);
        b.setVisibility(View.VISIBLE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminPage();
            }
        });
    }

    //methods

    private void openLoanCalc(){ // open loan page
        Intent intent = new Intent(this, LoanCalc.class);
        startActivity(intent);
    }

    private void openAdminPage(){
        if(mUser.getAdmin().equals(true)){
            Intent intent = new Intent(this, AdminPage.class);
            startActivity(intent);
        }else{
            View b = findViewById(R.id.adminButton);
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Sorry, not admin. Goodbye!", Toast.LENGTH_SHORT).show();
                    View b = findViewById(R.id.adminButton);
                    b.setVisibility(View.INVISIBLE);

                }
            });
        }

    }

    // scroll
    private void scroll(){
        mScroll = findViewById(R.id.DEBUG);
        mScroll.setMovementMethod(new ScrollingMovementMethod());
        List<User> users = mBankLogDAO.getAllUsers();

        StringBuilder sb = new StringBuilder();
        sb.append("All users: \n");

        for(User u : users){
            sb.append(u);
            sb.append("\n");
        }

        sb.append("All logs: \n");
        List<BankLog> logs = mBankLogDAO.getAllBankLogs();
        for(BankLog log : logs){
            sb.append(log);
        }

        mScroll.setText(sb.toString());
    }

    private void loginUser(int userId) {
        mUser = mBankLogDAO.getUserByUserId(userId);
        addUserToPreference(userId);
        invalidateOptionsMenu();
    }

    // menu button to log out
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mUser != null) {
            MenuItem item = menu.findItem(R.id.userLogoutMenu);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void addUserToPreference(int userId){
        if (mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void getDatabase(){
        mBankLogDAO = Room.databaseBuilder(this, BankAppDatabase.class, BankAppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getBankLogDAO();
    }

    private void checkForUser() {
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if (mUserId != -1) {
            return;
        }

        if (mPreferences == null) {
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if (mUserId != -1) {
            return;
        }

        // create predefined users mjim, admin, user1
        List<User> users = mBankLogDAO.getAllUsers();
        if (users.size() <= 0) {
            User defaultUser = new User("testuser1", "testuser1", false);
            //User altUser = new User("drew", "dac123", false);
            mBankLogDAO.insert(defaultUser);
            User user1 = new User("mjimenez","mj123",true);
            mBankLogDAO.insert(user1);
            User admin = new User("admin2", "admin2",true);
            mBankLogDAO.insert(admin);
        }

        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);

    }

    private void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    // log out action
    private void logoutUser() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.userLogout);
        alertBuilder.setMessage("Would you like to log out?");
        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        checkForUser();
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //We don't really need to do anything here.

                    }
                });

        alertBuilder.create().show();

    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
    }

    private BankLog getValuesFromDisplay(){
        String purchase = "No purchases";
        double amount = 0.0;
        int quantity = 0;

        purchase = mPurchase.getText().toString();

        try{
            amount = Double.parseDouble(mAmount.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("BANKLOG", "Could not convert amount");
        }

        try{
            quantity = Integer.parseInt(mQuantity.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("BANKLOG", "Could not convert quantity");
        }

        BankLog log = new BankLog(purchase,amount,quantity,mUserId);
        return log;
    }

    private void refreshDisplay() {
        mBankLogs = mBankLogDAO.getBankLogsByUserId(mUserId);

        if(mBankLogs.size() <= 0) {
            mMainDisplay.setText(R.string.noLogsMessage);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(BankLog log : mBankLogs){
            sb.append("\n");
            sb.append(log);
            sb.append("\n");
            sb.append("===============");
        }
        mMainDisplay.setText(sb.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userlogout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.userLogoutMenu:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }

}