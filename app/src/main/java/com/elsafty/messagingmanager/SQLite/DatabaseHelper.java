package com.elsafty.messagingmanager.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sms.db";
    private static final String CREATE_TABLE =
            "CREATE TABLE " + SmsContract.TABLE_NAME + " (" +
                    SmsContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SmsContract.COLUMN_NAME + " TEXT, " +
                    SmsContract.COLUMN_NUMBER + " TEXT, " +
                    SmsContract.COLUMN_SMS_TEXT + " TEXT, " +
                    SmsContract.COLUMN_DATE + " TEXT, " +
                    SmsContract.COLUMN_TIME + " TEXT, " +
                    SmsContract.COLUMN_STATUS + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
