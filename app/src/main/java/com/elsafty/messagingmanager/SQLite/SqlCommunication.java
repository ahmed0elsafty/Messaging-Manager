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
        // Get readable database
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        // Query the database for all messages where messageStatus is pending
        String selection = SmsContract.COLUMN_STATUS + "=?";
        String[] selectionArgs = {SmsContract.STATUS_SCHEDULED + ""};
        mResult = db.query(SmsContract.TABLE_NAME, null, selection, selectionArgs, null, null, "ASC");
        // Create list of messages objects
        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();

        // For number of messages retrieved create a messages object with name, number, message, message date, message time.

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
        // Get readable database
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        // Query the database for all messages where messageStatus is pending
        String selection = SmsContract.COLUMN_STATUS + "=?";
        String[] selectionArgs = {SmsContract.STATUS_SENT + ""};
        mResult = db.query(SmsContract.TABLE_NAME, null, selection, selectionArgs, null, null, "ASC");
        // Create list of messages objects
        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();

        // For number of messages retrieved create a messages object with name, number, message, message date, message time.

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
        // Get readable database
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        // Query the database for all messages where messageStatus is pending
        String selection = SmsContract.COLUMN_STATUS + "=?";
        String[] selectionArgs = {SmsContract.STATUS_FAILED + ""};
        mResult = db.query(SmsContract.TABLE_NAME, null, selection, selectionArgs, null, null, "ASC");
        // Create list of messages objects
        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();

        // For number of messages retrieved create a messages object with name, number, message, message date, message time.

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
        // Get readable database
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        // Query the database for all messages where messageStatus is pending
        String selection = SmsContract.COLUMN_STATUS + "=?";
        String[] selectionArgs = {SmsContract.STATUS_TRASHED + ""};
        mResult = db.query(SmsContract.TABLE_NAME, null, selection, selectionArgs, null, null, "ASC");
        // Create list of messages objects
        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();

        // For number of messages retrieved create a messages object with name, number, message, message date, message time.

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
        // Get readable database
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        // Query the database for all messages where messageStatus is pending
        String selection = SmsContract.COLUMN_ID + "=?";
        String[] selectionArgs =new String[] {String.valueOf(searchID)};
        mResult = db.rawQuery("SELECT * FROM Messages WHERE "+SmsContract.COLUMN_ID+"=" + searchID, null);
        // Create list of messages objects
        MyMessage messages = null;

        // For number of messages retrieved create a messages object with name, number, message, message date, message time.

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
        // Get the readable database.
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        // Create where clause from details of SMS object
        String selections = SmsContract.COLUMN_NAME + " = '" + message.getName() +
                "' AND " + SmsContract.COLUMN_NUMBER + " = '" + message.getNumber() +
                "' AND " + SmsContract.COLUMN_DATE + " = '" + message.getDate() +
                "' AND " + SmsContract.COLUMN_TIME + " = '" + message.getTime() +
                "' AND " + SmsContract.COLUMN_SMS_TEXT + " = '" + message.getTxtMessage() + "'";

        // Returns the number of affected rows. 0 means no rows were deleted.
        mResult = db.rawQuery("SELECT " + SmsContract.COLUMN_ID + " FROM " + SmsContract.TABLE_NAME +
                " WHERE " + selections, null);

        mResult.moveToFirst();
        return mResult.getInt(mResult.getColumnIndex(SmsContract.COLUMN_ID));
    }

    public void deleteMessage(int messageID) {
        // Get writable database
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        // Create where clause
        String selection = SmsContract.COLUMN_ID + " = '" + messageID + "'";

        // Remove row from table with messageID passed.
        db.delete(SmsContract.TABLE_NAME, selection, null);
    }

    public void updateMessageToSent(int messageID) {
        // Get writable database
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        // Put Sms ID into ContentValues object
        ContentValues contentValues = new ContentValues();
        contentValues.put(SmsContract.COLUMN_STATUS, SmsContract.STATUS_SENT);
        String selection = SmsContract.COLUMN_ID + "=?";
        String[] selectionArgs = {messageID+""};
        // Update messageStatus to Sent where SMSID is equal to SMS ID passed
        db.update(SmsContract.TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public void updateMessageToFailed(int messageID) {
        // Get writable database
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        // Put Sms ID into ContentValues object
        ContentValues contentValues = new ContentValues();
        contentValues.put(SmsContract.COLUMN_STATUS, SmsContract.STATUS_FAILED);
        String selection = SmsContract.COLUMN_ID + "=?";
        String[] selectionArgs = {messageID+""};
        // Update messageStatus to Sent where SMSID is equal to SMS ID passed
        db.update(SmsContract.TABLE_NAME, contentValues, selection, selectionArgs);
    }
    public void updateMessageToTrashed(int messageID) {
        // Get writable database
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        // Put Sms ID into ContentValues object
        ContentValues contentValues = new ContentValues();
        contentValues.put(SmsContract.COLUMN_STATUS, SmsContract.STATUS_TRASHED);
        String selection = SmsContract.COLUMN_ID + "=?";
        String[] selectionArgs = {messageID+""};
        // Update messageStatus to Sent where SMSID is equal to SMS ID passed
        db.update(SmsContract.TABLE_NAME, contentValues, selection, selectionArgs);
    }
}
