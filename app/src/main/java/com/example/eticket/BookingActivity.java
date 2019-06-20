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
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eticket.model.Booking;
import com.example.eticket.paypal.ConfirmationActivity;
import com.example.eticket.paypal.PaypalMainActivity;
import com.example.eticket.sqlite.DatabaseHelper;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class BookingActivity extends AppCompatActivity {

    TextInputLayout passenger_nameWrapper;
    TextView fare;
    Button book_button;
    String source = "", destination = "", line_type = "";
    RelativeLayout relative_layout;
    String totalFare = "";
    DatabaseHelper databaseHelper;
    String userId = "";
    String sourceLine = "";
    String destinationLine = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setTitle("Booking");

        passenger_nameWrapper = (TextInputLayout) findViewById(R.id.passenger_nameWrapper);
        relative_layout = (RelativeLayout) findViewById(R.id.relative_layout);
        fare = (TextView) findViewById(R.id.fare);
        book_button = (Button) findViewById(R.id.book_button);
        MaterialSpinner spinner_source = (MaterialSpinner) findViewById(R.id.spinner_source);
        spinner_source.setItems("Select Source", "Ottawa", "Toronto", "Brampton", "Mississauga", "Kitchener", "Surrey", "Vancouver", "Calgary", "British Columbia", "Victoria");
        spinner_source.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (position != 0) {
                    source = item;

                    if (destination.equals(source)) {
                        new AlertDialog.Builder(BookingActivity.this)
                                .setTitle("Alert")
                                .setMessage("Source and Destination cannot be same !")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).setCancelable(false)
                                .show();
                    } else {
                        source = item;
                        if (!destination.equals("")) {
                            fare.setVisibility(View.VISIBLE);
                            getLines(source, destination);
                            totalFare = getAmount(sourceLine, destinationLine);
                            fare.setText("Ticket Fare : " + totalFare);
                        }
                    }
                } else {
                    source = "";
                }
            }
        });
        MaterialSpinner spinner_destination = (MaterialSpinner) findViewById(R.id.spinner_destination);
        spinner_destination.setItems("Select Destination", "Ottawa", "Toronto", "Brampton", "Mississauga", "Kitchener", "Surrey", "Vancouver", "Calgary", "British Columbia", "Victoria");
        spinner_destination.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (position != 0) {
                    destination = item;

                    if (destination.equals(source)) {
                        new AlertDialog.Builder(BookingActivity.this)
                                .setTitle("Alert")
                                .setMessage("Source and Destination cannot be same !")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).setCancelable(false)
                                .show();
                    } else {
                        destination = item;
                        fare.setVisibility(View.VISIBLE);
                        getLines(source, destination);
                        totalFare = getAmount(sourceLine, destinationLine);
                        fare.setText("Ticket Fare : " + totalFare);
                    }

                } else {
                    destination = "";
                    fare.setVisibility(View.GONE);
                    totalFare = "0";
                    fare.setText("Ticket Fare : " + totalFare);
                    // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        MaterialSpinner spinner_line_type = (MaterialSpinner) findViewById(R.id.spinner_line_type);
        spinner_line_type.setItems("Select Line Type", "Central", "Southern", "Northern");
        spinner_line_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (position != 0) {
                    if (source.equals("") || destination.equals("")) {
                        new AlertDialog.Builder(BookingActivity.this)
                                .setTitle("Alert")
                                .setMessage("Source and Destination Required !")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).setCancelable(false)
                                .show();

                    } else {

                        line_type = item;
                    }

                } else {
                    line_type = "";
                }
            }
        });

        book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (source.equals("")) {
                    Snackbar.make(relative_layout, "Please Select Source", Snackbar.LENGTH_LONG).show();


                } else if (destination.equals("")) {
                    Snackbar.make(relative_layout, "Please Select Destination", Snackbar.LENGTH_LONG).show();


                } else if (source.equals(destination)) {
                    Snackbar.make(relative_layout, "Source and Destination can't be same", Snackbar.LENGTH_LONG).show();


                } else if (line_type.equals("")) {
                    Snackbar.make(relative_layout, "Please Select Line Type", Snackbar.LENGTH_LONG).show();


                } else if (passenger_nameWrapper.getEditText().getText().toString().equals("")) {
                    Snackbar.make(relative_layout, "Please Enter Passenger Name", Snackbar.LENGTH_LONG).show();


                } else if (totalFare.equals("0")) {
                    Snackbar.make(relative_layout, "Fare can't calculate", Snackbar.LENGTH_LONG).show();


                } else {


                    databaseHelper = new DatabaseHelper(BookingActivity.this);

                    Booking booking = new Booking();
                    SharedPreferences getpreferences = PreferenceManager.getDefaultSharedPreferences(BookingActivity.this);
                    userId = getpreferences.getString("userId", "");

                    if (Double.parseDouble(databaseHelper.getWalletAmount(userId)) < Double.parseDouble(totalFare)) {
                        new AlertDialog.Builder(BookingActivity.this)
                                .setTitle("Warning")
                                .setMessage(getString(R.string.add_money))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(BookingActivity.this, PaypalMainActivity.class));
                                        finish();
                                    }
                                }).setCancelable(false)
                                .show();

                    } else {
                        booking.setUserId(userId);
                        booking.setSource(source);
                        booking.setDestination(destination);
                        booking.setLineType(line_type);
                        booking.setAmount(totalFare);
                        booking.setPassengerName(passenger_nameWrapper.getEditText().getText().toString());

                        boolean isInsert = databaseHelper.addBooking(booking);
                        if (isInsert == true) {
                            Double finalPayment = Double.parseDouble(databaseHelper.getWalletAmount(userId)) - Double.parseDouble(totalFare);
                            boolean isUpdate = databaseHelper.updateWalletAmount(userId + "", finalPayment + "");
                            if (isUpdate == true)
                                Toast.makeText(BookingActivity.this, "Data Update", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(BookingActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();

                            Toast.makeText(BookingActivity.this, "Booking Successful", Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(BookingActivity.this, "Error in Booking", Toast.LENGTH_LONG).show();


                        }
                        // Snack Bar to show success message that record saved successfully
                        //  Snackbar.make(relative_layout, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();


                        new AlertDialog.Builder(BookingActivity.this)
                                .setTitle("Message")
                                .setMessage(getString(R.string.success_booking))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).setCancelable(false)
                                .show();

                    }
                }


            }
        });
    }

    public void getLines(String source, String destination) {

        if (source.equals("Kitchener") || source.equals("Surrey") || source.equals("Victoria")) {
            sourceLine = "Northern";
        }

        if (source.equals("Toronto") || source.equals("Brampton") || source.equals("Ottawa") || source.equals("Mississauga")) {
            sourceLine = "Central";
        }
        if (source.equals("British Columbia") || source.equals("Vancouver") || source.equals("Calgary")) {
            sourceLine = "Southern";
        }


        if (destination.equals("Kitchener") || destination.equals("Surrey") || destination.equals("Victoria")) {
            destinationLine = "Northern";
        }

        if (destination.equals("Toronto") || destination.equals("Brampton") || destination.equals("Ottawa") || destination.equals("Mississauga")) {
            destinationLine = "Central";
        }
        if (destination.equals("British Columbia") || destination.equals("Vancouver") || destination.equals("Calgary")) {
            destinationLine = "Southern";
        }


    }

    public String getAmount(String sourceLine, String destinationLine) {
        String amount = "";

        if (sourceLine.equals(destinationLine)) {
            amount = "20";
        } else if (sourceLine.equals("Northern") && destinationLine.equals("Central")) {
            amount = "100";

        } else if (sourceLine.equals("Central") && destinationLine.equals("Northern")) {
            amount = "100";

        } else if (sourceLine.equals("Northern") && destinationLine.equals("Southern")) {
            amount = "200";

        } else if (sourceLine.equals("Southern") && destinationLine.equals("Northern")) {
            amount = "200";

        } else if (sourceLine.equals("Central") && destinationLine.equals("Southern")) {
            amount = "100";

        } else if (sourceLine.equals("Southern") && destinationLine.equals("Central")) {
            amount = "100";

        }


        return amount;
    }
}
