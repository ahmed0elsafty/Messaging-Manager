package com.elsafty.messagingmanager.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elsafty.messagingmanager.Pojos.MyMessage;

import java.util.ArrayList;

public class SqlCommunication {
    private DatabaseHelper mDpHelper;
    private Cursor mResult;

    public SqlCommunication(Context context) {
        mResult = null;
        this.mDpHelper = new DatabaseHelper(context);
    }

    public int insertMessage(MyMessage message) {
        ContentValues rowData = new ContentValues();
        rowData.put(SmsContract.COLUMN_NAME, message.getName());
        rowData.put(SmsContract.COLUMN_NUMBER, message.getNumber());
        rowData.put(SmsContract.COLUMN_DATE, message.getDate());
        rowData.put(SmsContract.COLUMN_TIME, message.getTime());
        rowData.put(SmsContract.COLUMN_SMS_TEXT, message.getTxtMessage());
        rowData.put(SmsContract.COLUMN_STATUS, message.getStatus());


        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        int insertedSmsID = (int) db.insert(SmsContract.TABLE_NAME, null, rowData);
        db.close();
        return insertedSmsID;
    }

    public ArrayList<MyMessage> getScheduledList() {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String selection = SmsContract.COLUMN_STATUS + "=?";
        String[] selectionArgs = {SmsContract.STATUS_SCHEDULED + ""};
        mResult = db.query(SmsContract.TABLE_NAME, null, selection, selectionArgs, null, null, "ASC");
        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();


        while (mResult.moveToNext()) {
            mResult.moveToFirst();
            messages.add(new MyMessage(mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NAME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NUMBER)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_DATE)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_TIME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_SMS_TEXT)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_STATUS))));
        }
        return messages;
    }

    public ArrayList<MyMessage> getSentList() {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String selection = SmsContract.COLUMN_STATUS + "=?";
        String[] selectionArgs = {SmsContract.STATUS_SENT + ""};
        mResult = db.query(SmsContract.TABLE_NAME, null, selection, selectionArgs, null, null, "ASC");
        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();


        while (mResult.moveToNext()) {
            mResult.moveToFirst();
            messages.add(new MyMessage(mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NAME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NUMBER)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_DATE)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_TIME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_SMS_TEXT)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_STATUS))));
        }
        return messages;
    }

    public ArrayList<MyMessage> getFailedList() {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String selection = SmsContract.COLUMN_STATUS + "=?";
        String[] selectionArgs = {SmsContract.STATUS_FAILED + ""};
        mResult = db.query(SmsContract.TABLE_NAME, null, selection, selectionArgs, null, null, "ASC");
        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();

        while (mResult.moveToNext()) {
            mResult.moveToFirst();
            messages.add(new MyMessage(mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NAME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NUMBER)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_DATE)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_TIME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_SMS_TEXT)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_STATUS))));
        }
        return messages;
    }

    public ArrayList<MyMessage> getTrashedList() {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String selection = SmsContract.COLUMN_STATUS + "=?";
        String[] selectionArgs = {SmsContract.STATUS_TRASHED + ""};
        mResult = db.query(SmsContract.TABLE_NAME, null, selection, selectionArgs, null, null, "ASC");
        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();

        while (mResult.moveToNext()) {
            mResult.moveToFirst();
            messages.add(new MyMessage(mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NAME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NUMBER)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_DATE)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_TIME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_SMS_TEXT)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_STATUS))));
        }
        return messages;
    }

    public MyMessage getMessageById(int searchID) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String selection = SmsContract.COLUMN_ID + "=?";
        String[] selectionArgs =new String[] {String.valueOf(searchID)};
        mResult = db.rawQuery("SELECT * FROM Messages WHERE "+SmsContract.COLUMN_ID+"=" + searchID, null);
        MyMessage messages = null;


        while (mResult.moveToNext()) {
            mResult.moveToFirst();
            messages = new MyMessage(mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NAME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_NUMBER)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_DATE)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_TIME)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_SMS_TEXT)),
                    mResult.getString(mResult.getColumnIndex(SmsContract.COLUMN_STATUS)));
        }
        return messages;
    }


    public int retrieveMessageID(MyMessage message) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String selections = SmsContract.COLUMN_NAME + " = '" + message.getName() +
                "' AND " + SmsContract.COLUMN_NUMBER + " = '" + message.getNumber() +
                "' AND " + SmsContract.COLUMN_DATE + " = '" + message.getDate() +
                "' AND " + SmsContract.COLUMN_TIME + " = '" + message.getTime() +
                "' AND " + SmsContract.COLUMN_SMS_TEXT + " = '" + message.getTxtMessage() + "'";

        mResult = db.rawQuery("SELECT " + SmsContract.COLUMN_ID + " FROM " + SmsContract.TABLE_NAME +
                " WHERE " + selections, null);

        mResult.moveToFirst();
        return mResult.getInt(mResult.getColumnIndex(SmsContract.COLUMN_ID));
    }

    public void deleteMessage(int messageID) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        String selection = SmsContract.COLUMN_ID + " = '" + messageID + "'";

        db.delete(SmsContract.TABLE_NAME, selection, null);
    }

    public void updateMessageToSent(int messageID) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SmsContract.COLUMN_STATUS, SmsContract.STATUS_SENT);
        String selection = SmsContract.COLUMN_ID + "=?";
        String[] selectionArgs = {messageID+""};
        db.update(SmsContract.TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public void updateMessageToFailed(int messageID) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SmsContract.COLUMN_STATUS, SmsContract.STATUS_FAILED);
        String selection = SmsContract.COLUMN_ID + "=?";
        String[] selectionArgs = {messageID+""};
        db.update(SmsContract.TABLE_NAME, contentValues, selection, selectionArgs);
    }
    public void updateMessageToTrashed(int messageID) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SmsContract.COLUMN_STATUS, SmsContract.STATUS_TRASHED);
        String selection = SmsContract.COLUMN_ID + "=?";
        String[] selectionArgs = {messageID+""};
        db.update(SmsContract.TABLE_NAME, contentValues, selection, selectionArgs);
    }
}
