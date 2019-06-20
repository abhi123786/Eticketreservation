package com.example.eticket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OtherAppsScreen extends AppCompatActivity implements View.OnClickListener {
    Button open_mmt, open_trip, open_ixigo, open_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_apps_screen);
        open_mmt = (Button) findViewById(R.id.open_mmt);
        open_trip = (Button) findViewById(R.id.open_trip);
        open_ixigo = (Button) findViewById(R.id.open_ixigo);
        open_clear = (Button) findViewById(R.id.open_clear);


        open_ixigo.setOnClickListener(this);
        open_mmt.setOnClickListener(this);
        open_trip.setOnClickListener(this);
        open_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.open_mmt:
                reDirectToWebScreen("https://planner.makemytrip.com/");
                break;

            case R.id.open_ixigo:

                reDirectToWebScreen("https://www.ixigo.com/");
                break;

            case R.id.open_trip:

                reDirectToWebScreen("https://www.tripadvisor.in/");
                break;

            case R.id.open_clear:

                reDirectToWebScreen("https://www.cleartrip.com/");
                break;
            default:
                break;
        }
    }

    public void reDirectToWebScreen(String url) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("url", url);
        editor.apply();
        startActivity(new Intent(OtherAppsScreen.this, WebViewActivity.class));

    }
}
