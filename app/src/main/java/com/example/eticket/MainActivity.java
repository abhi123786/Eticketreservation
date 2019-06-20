package com.example.eticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eticket.paypal.PaypalMainActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton, wallet, book_ticket, view_ticket, other_apps, trip_planner;
    private String status = "", walletAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences getpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        walletAmount = getpreferences.getString("walletamount", "");
        if (walletAmount.equals("")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("walletamount", "0");
            editor.apply();
        }

        loginButton = (Button) findViewById(R.id.login);
        book_ticket = (Button) findViewById(R.id.book_ticket);
        wallet = (Button) findViewById(R.id.wallet);
        view_ticket = (Button) findViewById(R.id.view_ticket);
        other_apps = (Button) findViewById(R.id.other_apps);
        trip_planner = (Button) findViewById(R.id.trip_planner);


        loginButton.setOnClickListener(this);
        book_ticket.setOnClickListener(this);
        wallet.setOnClickListener(this);
        view_ticket.setOnClickListener(this);
        other_apps.setOnClickListener(this);
        trip_planner.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.login:
                if (status.equals("")) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Message")
                            .setMessage("Do you want to logout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("status", "");
                                    editor.apply();
                                    status = "";
                                    loginButton.setText("Login");
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }


                break;
            case R.id.wallet:
                if (!status.equalsIgnoreCase("")) {
                    startActivity(new Intent(MainActivity.this, PaypalMainActivity.class));
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Warning")
                            .setMessage(getString(R.string.login_first))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                            }).setCancelable(false)
                            .show();

                }
                break;
            case R.id.book_ticket:
                if (!status.equalsIgnoreCase("")) {
                    startActivity(new Intent(MainActivity.this, BookingActivity.class));
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Warning")
                            .setMessage(getString(R.string.login_first))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                            }).setCancelable(false)
                            .show();

                }
                break;
            case R.id.view_ticket:
                if (!status.equalsIgnoreCase("")) {
                    startActivity(new Intent(MainActivity.this, BookingListActivity.class));
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Warning")
                            .setMessage(getString(R.string.login_first))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                            }).setCancelable(false)
                            .show();
                }
                break;
            case R.id.other_apps:
                startActivity(new Intent(MainActivity.this, OtherAppsScreen.class));
                break;
            case R.id.trip_planner:
                startActivity(new Intent(MainActivity.this, TripPlannerActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        status = preferences.getString("status", "");
        if (!status.equalsIgnoreCase("")) {
            loginButton.setText("Logout");

        } else {

            loginButton.setText("Login");
        }

    }
}

