package com.example.eticket;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class TripPlannerActivity extends AppCompatActivity {
    String source = "", destination = "";
    TextView fare;
    String totalFare = "", sourceLine = "", destinationLine = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);
        setTitle("Plan your Trip");
        fare = (TextView) findViewById(R.id.fare);

        MaterialSpinner spinner_source = (MaterialSpinner) findViewById(R.id.spinner_source);
        spinner_source.setItems("Select Source", "Ottawa", "Toronto", "Brampton", "Mississauga", "Kitchener", "Surrey", "Vancouver", "Calgary", "British Columbia", "Victoria");
        spinner_source.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (position != 0) {
                    source = item;

                    if (destination.equals(source)) {
                        new AlertDialog.Builder(TripPlannerActivity.this)
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
                        new AlertDialog.Builder(TripPlannerActivity.this)
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
