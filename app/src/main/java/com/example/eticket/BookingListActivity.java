package com.example.eticket;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.eticket.model.Booking;
import com.example.eticket.model.User;
import com.example.eticket.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class BookingListActivity extends AppCompatActivity {

    private AppCompatActivity activity = BookingListActivity.this;
    private RecyclerView recyclerViewUsers;
    private List<Booking> bookingList;
    private BookingRecyclerAdapter bookingRecyclerAdapter;
    private DatabaseHelper databaseHelper;
    String userid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_list);
        getSupportActionBar().setTitle("My Bookings");
        initViews();
        initObjects();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        SharedPreferences getpreferences = PreferenceManager.getDefaultSharedPreferences(BookingListActivity.this);
        userid = getpreferences.getString("userId", "");
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        bookingList = new ArrayList<>();
        bookingRecyclerAdapter = new BookingRecyclerAdapter(bookingList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(bookingRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);


        getDataFromSQLite();
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                bookingList.clear();
                bookingList.addAll(databaseHelper.getAllBookings(userid));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                bookingRecyclerAdapter.notifyDataSetChanged();

                if (bookingList.size() == 0) {

                    new AlertDialog.Builder(BookingListActivity.this)
                            .setTitle("Booking")
                            .setMessage("You have no bookings !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setCancelable(false)
                            .show();
                }
            }
        }.execute();
    }
}