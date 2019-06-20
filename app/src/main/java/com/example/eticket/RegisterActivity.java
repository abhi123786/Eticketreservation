package com.example.eticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.eticket.model.User;
import com.example.eticket.sqlite.DatabaseHelper;
import com.example.validation.InputValidation;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView already_registered;
    private Button register;
    private TextInputLayout firstNameWrapper, lastNameWrapper, emailWrapper, phoneWrapper, usernameWrapper, passwordWrapper, confrimPasswordWrapper;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    private RelativeLayout relative_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(getResources().getString(R.string.register));
        initObjects();
        initViews();
        initListners();


    }

    public void initListners() {

        already_registered.setOnClickListener(this);
        register.setOnClickListener(this);


    }

    public void initViews() {
        relative_layout = (RelativeLayout) findViewById(R.id.relative_layout);
        register = (Button) findViewById(R.id.register);
        already_registered = (TextView) findViewById(R.id.already_registered);
        firstNameWrapper = (TextInputLayout) findViewById(R.id.firstNameWrapper);
        lastNameWrapper = (TextInputLayout) findViewById(R.id.lastNameWrapper);
        emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        phoneWrapper = (TextInputLayout) findViewById(R.id.phoneWrapper);
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.password_wrapper);
        confrimPasswordWrapper = (TextInputLayout) findViewById(R.id.confirmPassword_wrapper);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.already_registered:
                finish();
                break;


            case R.id.register:

                postDataToSQLite();
                break;


            default:
                break;

        }

    }


    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(firstNameWrapper.getEditText(), firstNameWrapper, getString(R.string.error_message_first))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(lastNameWrapper.getEditText(), lastNameWrapper, getString(R.string.error_message_last))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(emailWrapper.getEditText(), emailWrapper, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(phoneWrapper.getEditText(), phoneWrapper, getString(R.string.error_message_phone))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(usernameWrapper.getEditText(), usernameWrapper, getString(R.string.error_message_user))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(passwordWrapper.getEditText(), passwordWrapper, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(confrimPasswordWrapper.getEditText(), confrimPasswordWrapper, getString(R.string.error_message_confirm_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(passwordWrapper.getEditText(), confrimPasswordWrapper.getEditText(),
                confrimPasswordWrapper, getString(R.string.error_password_match))) {
            return;
        }

        if (!databaseHelper.checkUser(emailWrapper.getEditText().getText().toString().trim())) {

            user.setName(usernameWrapper.getEditText().getText().toString().trim());
            user.setEmail(emailWrapper.getEditText().getText().toString().trim());
            user.setPassword(passwordWrapper.getEditText().getText().toString().trim());
            user.setFirstName(firstNameWrapper.getEditText().getText().toString().trim());
            user.setLastName(lastNameWrapper.getEditText().getText().toString().trim());
            user.setPhone(phoneWrapper.getEditText().getText().toString().trim());
            user.setWalletAmount("0.0");

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
          //  Snackbar.make(relative_layout, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();


            new AlertDialog.Builder(this)
                    .setTitle("Message")
                    .setMessage(getString(R.string.success_message))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setCancelable(false)
                    .show();





        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(relative_layout, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }


    }


    private void initObjects() {
        inputValidation = new InputValidation(RegisterActivity.this);
        databaseHelper = new DatabaseHelper(RegisterActivity.this);
        user = new User();

    }

    private void emptyInputEditText() {
        firstNameWrapper.getEditText().setText(null);
        lastNameWrapper.getEditText().setText(null);
        phoneWrapper.getEditText().setText(null);
        usernameWrapper.getEditText().setText(null);
        emailWrapper.getEditText().setText(null);
        passwordWrapper.getEditText().setText(null);
        confrimPasswordWrapper.getEditText().setText(null);
    }
}
