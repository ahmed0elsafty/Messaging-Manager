package com.elsafty.messagingmanager.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessagingDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "messaging.db";

    private static final String CREATE_Messaging_TABLE =
            "CREATE TABLE " + SmsContract.TABLE_NAME + " (" +
                    SmsContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SmsContract.COLUMN_GROUP_ID + " INTEGER, " +
                    SmsContract.COLUMN_SMS_TEXT + " TEXT, " +
                    SmsContract.COLUMN_DATE + " TEXT, " +
                    SmsContract.COLUMN_TIME + " TEXT, " +
                    SmsContract.COLUMN_STATUS + " INTEGER);";

    private static final String CREATE_Groups_TABLE =
            "CREATE TABLE " + GroupContract.TABLE_NAME + " (" +
                    GroupContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GroupContract.COLUMN_NAME + " TEXT, " +
                    GroupContract.COLUMN_MEMBERS + " INTEGER);";

    private static final String CREATE_Groups_Members_TABLE =
            "CREATE TABLE " + GroupMembersContract.TABLE_NAME + " (" +
                    GroupMembersContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GroupMembersContract.COLUMN_CONTACT_ID + " INTEGER, " +
                    GroupMembersContract.COLUMN_GROUP_ID + " INTEGER);";

    private static final String CREATE_Contacts_TABLE =
            "CREATE TABLE " + ContactContract.TABLE_NAME + " (" +
                    ContactContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ContactContract.COLUMN_NAME + " TEXT, " +
                    ContactContract.COLUMN_NUMBER + " TEXT);";

    private static final String CREATE_Contacts_Messages_TABLE =
            "CREATE TABLE " + ContactMessageContract.TABLE_NAME + " (" +
                    ContactMessageContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ContactMessageContract.COLUMN_CONTACT_ID + " INTEGER, " +
                    ContactMessageContract.COLUMN_Message_ID + " INTEGER);";

    public MessagingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_Messaging_TABLE);
            db.execSQL(CREATE_Groups_TABLE);
            db.execSQL(CREATE_Groups_Members_TABLE);
            db.execSQL(CREATE_Contacts_TABLE);
            db.execSQL(CREATE_Contacts_Messages_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}
