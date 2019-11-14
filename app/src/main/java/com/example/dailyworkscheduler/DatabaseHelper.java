package com.example.dailyworkscheduler;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.dailyworkscheduler.model.User;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "TIME_ACTIVITY";
    //database name-scheduler database
    public static final String DATABASE_NAME="scheduler.db";
    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";
    public static final String TABLE_NAME="to_schedule";
    public static final String TABLE_NAME2="free_hours";

    //columns in to_schedule table

    public static final String col_1="t_workid ";
    public static final String col_2="workname";
    public static final String col_3="priority";
    public static final String col_4="req_time";


    //columns in free_hours table

    public static final String col_6="free_time_id";
    public static final String col_7="time_from";
    public static final String col_8="time_to";

    //database is created here

    public DatabaseHelper(Activity2 context ) {
        //database is created by calling this constructor
        super(context,DATABASE_NAME , null, 1);

    }

    public DatabaseHelper(MainActivity context ) {
        //database is created by calling this constructor
        super(context,DATABASE_NAME , null, 1);

    }
    public DatabaseHelper(MyService context ) {
        //database is created by calling this constructor
        super(context,DATABASE_NAME , null, 1);

    }
    public DatabaseHelper(LoginActivity context ) {
        //database is created by calling this constructor
        super(context,DATABASE_NAME , null, 1);

    }
    public DatabaseHelper(RegisterActivity context ) {
        //database is created by calling this constructor
        super(context,DATABASE_NAME , null, 1);

    }

    //tables are created here

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+ TABLE_NAME + "(t_workid integer primary key autoincrement ,workname text ,priority integer ,req_time time)");
        Log.i(TAG, "to_schedule table is created");
        db.execSQL("create table "+ TABLE_NAME2+ "(free_time_id integer primary key autoincrement ,time_from time ,time_to time)");
        Log.i(TAG, "free hours table is created");
        db.execSQL(CREATE_USER_TABLE);
        Log.i(TAG,"user table is created");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
        onCreate(db);
    }

   public boolean create_to_schedule(String w_name ,String prior,String req_time){


        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col_2,w_name);
        contentValues.put(col_3,prior);
        contentValues.put(col_4,req_time);


        long result=db.insert(TABLE_NAME,null,contentValues);


       Log.i("dhivya",     "result value is : " + result);

        if(result==-1)
            return false;
        else
            return true;

    }
    public boolean create_time_hours(String timefrom,String timeto){


        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues timeValues=new ContentValues();
        timeValues.put(col_7,timefrom);
        timeValues.put(col_8,timeto);

        long result1=db.insert(TABLE_NAME2,null,timeValues);
        Log.i("suba",     "time inserted value is : " + result1);

        if(result1==-1){
            Log.i(TAG, "values not added in free hours table");
            return false;
        }

        else{
            Log.i(TAG, "values added in free hours table");
            return true;
    }}

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, values);

    }

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




    public Cursor retrievework(){
        //query for selecting work with maximum priority
        SQLiteDatabase mydb=this.getReadableDatabase();
        Cursor cursor =mydb.rawQuery("select max(workname) from to_schedule order by priority desc",null);
        if(cursor.getCount()!=0) Log.i("query","has data");
        return cursor;
    }
    public Cursor retrievetimefrom(){
        //query for selecting work with maximum priority
        SQLiteDatabase mydb=this.getReadableDatabase();
        Cursor cursor =mydb.rawQuery("select time_from  from free_hours where time_from>CURRENT_TIME ",null);
        if(cursor.getCount()!=0) Log.i("query","has data");
        return cursor;
    }



    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
