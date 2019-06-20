package com.example.eticket.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.eticket.LoginActivity;
import com.example.eticket.model.Booking;
import com.example.eticket.model.User;

import static java.sql.Types.REAL;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";


    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_WALLET = "user_wallet";

    // booking table name
    private static final String TABLE_BOOKINGS = "bookings";

    // booking Table Columns names
    private static final String COLUMN_BOOKING_ID = "booking_id";
    private static final String COLUMN_USER_BOOKING_ID = "booking_user_id";
    private static final String COLUMN_SOURCE = "source";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_LINE_TYPE = "line_type";
    private static final String COLUMN_PASSENGER_NAME = "passenger_name";
    private static final String COLUMN_AMOUNT = "amount";


    // create Users table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_WALLET + " REAL" + ")";
    // create Bookings table sql query
    private String CREATE_BOOKING_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
            + COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_BOOKING_ID + " TEXT,"
            + COLUMN_SOURCE + " TEXT," + COLUMN_DESTINATION + " TEXT," + COLUMN_LINE_TYPE + " TEXT," + COLUMN_PASSENGER_NAME + " TEXT," + COLUMN_AMOUNT + " TEXT" + ")";


    // drop User table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    // drop Booking table sql query
    private String DROP_Booking_TABLE = "DROP TABLE IF EXISTS " + TABLE_BOOKINGS;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_BOOKING_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_Booking_TABLE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_WALLET, user.getWalletAmount());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    public boolean addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_BOOKING_ID, booking.getUserId());
        values.put(COLUMN_SOURCE, booking.getSource());
        values.put(COLUMN_DESTINATION, booking.getDestination());
        values.put(COLUMN_LINE_TYPE, booking.getLineType());
        values.put(COLUMN_PASSENGER_NAME, booking.getPassengerName());
        values.put(COLUMN_AMOUNT, booking.getAmount());

        // Inserting Row
        db.insert(TABLE_BOOKINGS, null, values);

        db.close();
        return true;
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();

        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public int getId(String email, String password) {
        int id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_id FROM user WHERE user_email=? AND user_password=?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

    public String getWalletAmount(String userId) {
        String walletAmount = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_wallet FROM user WHERE user_id=?", new String[]{userId});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            walletAmount = cursor.getFloat(0) + "";
            cursor.close();
        }
        return walletAmount;
    }


    public String getuserName(String userId) {
        String username = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_name FROM user WHERE user_id=?", new String[]{userId});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            username = cursor.getString(0) + "";
            cursor.close();
        }
        return username;
    }

    public boolean updateWalletAmount(String userid, String wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_ID, userid);
        contentValues.put(COLUMN_USER_WALLET, wallet);
        db.update(TABLE_USER, contentValues, "user_id = ?", new String[]{userid});
        return true;
    }


    /**
     * This method is to fetch all bookings of user and get records according to user
     *
     * @return list
     */
    public List<Booking> getAllBookings(String userid) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_BOOKING_ID, COLUMN_USER_BOOKING_ID, COLUMN_SOURCE, COLUMN_DESTINATION, COLUMN_LINE_TYPE, COLUMN_PASSENGER_NAME, COLUMN_AMOUNT
        };
        // sorting orders
        String sortOrder =
                COLUMN_BOOKING_ID + " ASC";
        List<Booking> bookingList = new ArrayList<Booking>();

        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_BOOKING_ID + " = ?";

        // selection argument
        String[] selectionArgs = {userid};
        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_BOOKINGS, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setBookingId(cursor.getString(cursor.getColumnIndex(COLUMN_BOOKING_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndex(COLUMN_USER_BOOKING_ID)));
                booking.setAmount(cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT)));
                booking.setSource(cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE)));
                booking.setDestination(cursor.getString(cursor.getColumnIndex(COLUMN_DESTINATION)));
                booking.setPassengerName(cursor.getString(cursor.getColumnIndex(COLUMN_PASSENGER_NAME)));
                booking.setLineType(cursor.getString(cursor.getColumnIndex(COLUMN_LINE_TYPE)));
                // Adding user record to list
                bookingList.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return bookingList;
    }


}