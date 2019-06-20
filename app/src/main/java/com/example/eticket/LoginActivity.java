package com.example.eticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eticket.model.Booking;
import com.example.eticket.model.User;
import com.example.eticket.sqlite.DatabaseHelper;
import com.example.validation.InputValidation;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    TextView register_here;
    Button login_button;
    InputValidation inputValidation;
    DatabaseHelper databaseHelper;
    RelativeLayout relative_layout;
    TextInputLayout usernameWrapper, password_wrapper;
    public static int userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getResources().getString(R.string.login));
        initViews();
        initObjects();
        initListeners();


    }


    public void initViews() {
        relative_layout = (RelativeLayout) findViewById(R.id.relative_layout);
        register_here = (TextView) findViewById(R.id.register_here);
        login_button = (Button) findViewById(R.id.login_button);
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        password_wrapper = (TextInputLayout) findViewById(R.id.password_wrapper);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.login_button:
                verifyFromSQLite();
                break;


            case R.id.register_here:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            default:
                break;

        }

    }

    private void initObjects() {
        inputValidation = new InputValidation(LoginActivity.this);
        databaseHelper = new DatabaseHelper(LoginActivity.this);


    }

    private void initListeners() {
        register_here.setOnClickListener(this);
        login_button.setOnClickListener(this);
    }

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(usernameWrapper.getEditText(), usernameWrapper, getString(R.string.error_message_user))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(password_wrapper.getEditText(), password_wrapper, getString(R.string.error_message_password))) {
            return;
        }


        if (databaseHelper.checkUser(usernameWrapper.getEditText().getText().toString().trim()
                , password_wrapper.getEditText().getText().toString().trim())) {
            //           Toast.makeText(LoginActivity.this, databaseHelper. + "", Toast.LENGTH_SHORT).show();
            final int userid = databaseHelper.getId(usernameWrapper.getEditText().getText().toString(), password_wrapper.getEditText().getText().toString());
            final String walletAmount = databaseHelper.getWalletAmount(userid + "");

            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Login Successful !")
                    .setMessage("Welcome " + databaseHelper.getuserName(userid + ""))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("status", "loggedin");
                            editor.putString("userId", userid + "");
                            editor.apply();

                            boolean isUpdate = databaseHelper.updateWalletAmount(userid + "", walletAmount);
                            if (isUpdate == true)
                                Toast.makeText(LoginActivity.this, "Data Update", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(LoginActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();

                            emptyInputEditText();
                            finish();
                        }
                    }).setCancelable(false)
                    .show();


        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(relative_layout, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }

    }


    private void emptyInputEditText() {
        usernameWrapper.getEditText().setText(null);
        password_wrapper.getEditText().setText(null);

    }

}

