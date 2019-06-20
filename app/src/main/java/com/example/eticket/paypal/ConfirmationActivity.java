package com.example.eticket.paypal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.eticket.LoginActivity;
import com.example.eticket.MainActivity;
import com.example.eticket.R;
import com.example.eticket.sqlite.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmationActivity extends AppCompatActivity {
    TextView textpayment, textViewId, textViewStatus, textViewAmount, payment_id, status_a, payment_a;
    ImageView img_a;
    String walletAmount = "";
    Double totalAmount;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        databaseHelper = new DatabaseHelper(ConfirmationActivity.this);
        //Getting Intent
        Intent intent = getIntent();


        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


       /* Typeface font = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        textpayment.setTypeface(font);
        textViewId.setTypeface(font);
        textViewStatus.setTypeface(font);
        textViewAmount.setTypeface(font);
        payment_id.setTypeface(font);
        status_a.setTypeface(font);
        payment_a.setTypeface(font);*/


        img_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hh = new Intent(ConfirmationActivity.this, MainActivity.class);
                startActivity(hh);
                finishAffinity();
            }
        });


    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        textViewId = (TextView) findViewById(R.id.paymentId);
        textViewStatus = (TextView) findViewById(R.id.paymentStatus);
        textViewAmount = (TextView) findViewById(R.id.paymentAmount);
        textpayment = (TextView) findViewById(R.id.textpayment);
        payment_id = (TextView) findViewById(R.id.payment_id);
        status_a = (TextView) findViewById(R.id.status_a);
        payment_a = (TextView) findViewById(R.id.payment_a);
        img_a = (ImageView) findViewById(R.id.img_a);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount + " USD");

        SharedPreferences getpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userid = getpreferences.getString("userId", "");
        String walletAmount = databaseHelper.getWalletAmount(userid + "");


        totalAmount = Double.parseDouble(walletAmount) + Double.parseDouble(paymentAmount);

        boolean isUpdate = databaseHelper.updateWalletAmount(userid + "", totalAmount + "");
        if (isUpdate == true)
            Toast.makeText(ConfirmationActivity.this, "Data Update", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(ConfirmationActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent hh = new Intent(ConfirmationActivity.this, MainActivity.class);
        startActivity(hh);
        finishAffinity();
    }
}
