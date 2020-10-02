package com.elsafty.messagingmanager.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elsafty.messagingmanager.Pojos.MyContact;
import com.elsafty.messagingmanager.Pojos.MyGroup;
import com.elsafty.messagingmanager.Pojos.MyMessage;
import com.elsafty.messagingmanager.Pojos.RealMessage;

import java.util.ArrayList;

public class SqlCommunication {
    private static final String TAG = "SqlCommunication";
    private MessagingDbHelper mDpHelper;


    public SqlCommunication(Context context) {
        this.mDpHelper = new MessagingDbHelper(context);
    }

    // All Contact queries
    public int insertContact(MyContact contact) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();
        ContentValues rowData = new ContentValues();
        rowData.put(ContactContract.COLUMN_NAME, contact.getName());
        rowData.put(ContactContract.COLUMN_NUMBER, contact.getNumber());
        int id = (int) db.insert(ContactContract.TABLE_NAME, null, rowData);
        return id;
    }

    public void insertAllContacts(ArrayList<MyContact> contacts) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        for (MyContact contact : contacts) {
            ContentValues rowData = new ContentValues();
            rowData.put(ContactContract.COLUMN_NAME, contact.getName());
            rowData.put(ContactContract.COLUMN_NUMBER, contact.getNumber());
            db.insert(ContactContract.TABLE_NAME, null, rowData);
        }
    }

    public ArrayList<MyContact> getAllContacts() {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String[] projection = {ContactContract.COLUMN_NAME, ContactContract.COLUMN_NUMBER};
        Cursor result = db.query(ContactContract.TABLE_NAME, projection, null, null, null, null, ContactContract.COLUMN_NAME);
        ArrayList<MyContact> contacts = new ArrayList<MyContact>();


        while (result.moveToNext()) {

            contacts.add(new MyContact(result.getString(result.getColumnIndex(ContactContract.COLUMN_NAME)),
                    result.getString(result.getColumnIndex(ContactContract.COLUMN_NUMBER))));
        }
        db.close();
        return contacts;
    }

    public MyContact getContactById(int searchID) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();
        String queryStatement = "SELECT * FROM " + ContactContract.TABLE_NAME + " WHERE " + ContactContract.COLUMN_ID + "=" + searchID;
        Cursor result = db.rawQuery(queryStatement, null);
        MyContact contact = null;


        while (result.moveToNext()) {
            contact = new MyContact(result.getString(result.getColumnIndex(ContactContract.COLUMN_NAME)),
                    result.getString(result.getColumnIndex(ContactContract.COLUMN_NUMBER)));
        }
        db.close();
        return contact;
    }

    public int getContactId(MyContact contact) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        /*String selections = ContactContract.COLUMN_NAME + " == '" + contact.getName() +
                "' AND " + ContactContract.COLUMN_NUMBER + " == '" + contact.getNumber() +"'";

        String queryStatement = "SELECT " + ContactContract.COLUMN_ID + " FROM " + ContactContract.TABLE_NAME +
                " WHERE " + selections;

        Cursor result = db.rawQuery(queryStatement, null);*/
        String[] projection = {ContactContract.COLUMN_ID};
        String selections = ContactContract.COLUMN_NAME + "=? AND " +
                ContactContract.COLUMN_NUMBER + "=?";
        String[] args = {String.valueOf(contact.getName()), String.valueOf(contact.getNumber())};
        Cursor result = db.query(ContactContract.TABLE_NAME, null, selections, args, null, null, null);
        int id = -1;
        if (result.moveToNext()) {
            id = result.getInt(result.getColumnIndex(ContactContract.COLUMN_ID));
        }
        return id;
    }

    // All Message queries
    public int insertMessage(MyMessage message, int conatctId) {

        ContentValues rowData = new ContentValues();
        rowData.put(SmsContract.COLUMN_GROUP_ID, Integer.parseInt(message.getGroupId()));
        rowData.put(SmsContract.COLUMN_SMS_TEXT, message.getTxtMessage());
        rowData.put(SmsContract.COLUMN_DATE, message.getDate());
        rowData.put(SmsContract.COLUMN_TIME, message.getTime());
        rowData.put(SmsContract.COLUMN_STATUS, Integer.parseInt(message.getStatus()));

        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        int insertedSmsID = (int) db.insert(SmsContract.TABLE_NAME, null, rowData);
        insertContactMessage(conatctId, insertedSmsID);
        db.close();
        return insertedSmsID;
    }

    /*public ArrayList<MyMessage> getMessagesList(int status) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        Cursor result = db.query(SmsContract.TABLE_NAME, null,null,null,null,null,null);
        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();


        while (result.moveToNext()) {

            messages.add(new MyMessage(result.getString(result.getColumnIndex(SmsContract.COLUMN_GROUP_ID)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_SMS_TEXT)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_DATE)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_TIME)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_STATUS))));
        }
        db.close();
        return messages;
    }*/

    public MyMessage getMessageById(int searchID) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();
        String selection = SmsContract.COLUMN_ID + "=?";
        String[] args = {String.valueOf(searchID)};
        Cursor result = db.query(SmsContract.TABLE_NAME,null,selection,args,null,null,null);
        MyMessage message = null;

        while (result.moveToNext()) {

            message = new MyMessage(result.getString(result.getColumnIndex(SmsContract.COLUMN_GROUP_ID)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_SMS_TEXT)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_DATE)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_TIME)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_STATUS)));
        }
        db.close();
        return message;
    }


    public int getMessageID(MyMessage message) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String selections = SmsContract.COLUMN_GROUP_ID + " IS " + message.getGroupId() +
                " AND " + SmsContract.COLUMN_SMS_TEXT + " IS '" + message.getTxtMessage() +
                "' AND " + SmsContract.COLUMN_DATE + " IS '" + message.getDate() +
                "' AND " + SmsContract.COLUMN_TIME + " IS '" + message.getTime() +
                "' AND " + SmsContract.COLUMN_STATUS + " IS " + message.getStatus() ;
        String query = "SELECT " + SmsContract.COLUMN_ID + " FROM " + SmsContract.TABLE_NAME +
                " WHERE " + selections;

        Cursor result = db.rawQuery(query, null);
        result.moveToFirst();
        int id = result.getInt(result.getColumnIndex(SmsContract.COLUMN_ID));
        return id;
    }
    public ArrayList<RealMessage> getMessagesList(int messageStatus){
        SQLiteDatabase db = mDpHelper.getReadableDatabase();
        String queryStatement = "SELECT "+SmsContract.TABLE_NAME+".* ,"+ContactContract.COLUMN_NAME +
                " FROM "+ContactMessageContract.TABLE_NAME+" INNER JOIN "+SmsContract.TABLE_NAME+
                " ON " + SmsContract.COLUMN_ID + " = "+ContactMessageContract.COLUMN_Message_ID+
                " AND "+SmsContract.COLUMN_STATUS + " = "+messageStatus+" AND "+ ContactMessageContract.COLUMN_CONTACT_ID +
                " IS NOT NULL"+" INNER JOIN "+ContactContract.TABLE_NAME + " ON "+ContactContract.COLUMN_ID+
                " = "+ContactMessageContract.COLUMN_CONTACT_ID + " UNION "+
                "SELECT "+SmsContract.TABLE_NAME+".* ,"+GroupContract.COLUMN_NAME +
                " FROM "+ContactMessageContract.TABLE_NAME+" INNER JOIN "+SmsContract.TABLE_NAME+
                " ON " + SmsContract.COLUMN_ID + " = "+ContactMessageContract.COLUMN_Message_ID+
                " AND "+SmsContract.COLUMN_STATUS + " = "+messageStatus+" AND "+ ContactMessageContract.COLUMN_CONTACT_ID +
                " IS NULL"+" INNER JOIN "+GroupContract.TABLE_NAME + " ON "+GroupContract.COLUMN_ID+
                " = "+SmsContract.COLUMN_GROUP_ID;
        Cursor result = db.rawQuery(queryStatement,null);
        ArrayList<RealMessage> messages = new ArrayList<>();
        while (result.moveToNext()){
            messages.add(new RealMessage(String.valueOf(result.getInt(result.getColumnIndex(SmsContract.COLUMN_GROUP_ID))),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_SMS_TEXT)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_DATE)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_TIME)),
                    String.valueOf(result.getInt(result.getColumnIndex(SmsContract.COLUMN_STATUS))),
                    result.getString(result.getColumnIndex(ContactContract.COLUMN_NAME))));
        }
        return messages;

    }
    public int getContactIdByMessageID(int messageID){
        SQLiteDatabase db = mDpHelper.getReadableDatabase();
        String[] projection = {ContactMessageContract.COLUMN_CONTACT_ID};
        String selection = ContactMessageContract.COLUMN_Message_ID+"=?";
        String[] args = {String.valueOf(messageID)};
        Cursor result = db.query(ContactMessageContract.TABLE_NAME,projection,selection,args,null,null,null);
        return result.getInt(result.getColumnIndex(ContactMessageContract.COLUMN_CONTACT_ID));
    }

    public int deleteMessage(int messageID) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        String selection = SmsContract.COLUMN_ID + " = '" + messageID + "'";

        int id = db.delete(SmsContract.TABLE_NAME, selection, null);
        return id;
    }

    public int updateMessage(int messageID, int status) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SmsContract.COLUMN_STATUS, status);
        String selection = SmsContract.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(messageID)};
        int id = db.update(SmsContract.TABLE_NAME, contentValues, selection, selectionArgs);
        return id;
    }

    public int getContactIdByNumber(String number){
        SQLiteDatabase db = mDpHelper.getReadableDatabase();
        String[] projection = {ContactContract.COLUMN_ID};
        String selection = ContactContract.COLUMN_NUMBER + "=?";
        String[] args = {number};
        Cursor result = db.query(ContactContract.TABLE_NAME,projection,selection,args,null,null,null);
        return result.getInt(result.getColumnIndex(ContactContract.COLUMN_ID));
    }


    //All Group queries
    public int insertGroup(MyGroup group) {
        ContentValues rowData = new ContentValues();
        rowData.put(GroupContract.COLUMN_NAME, group.getName());
        rowData.put(GroupContract.COLUMN_MEMBERS, group.getMembers());

        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        int insertedGroupID = (int) db.insert(GroupContract.TABLE_NAME, null, rowData);

        return insertedGroupID;
    }

    public void insertAllGroups(ArrayList<MyGroup> groups) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        for (MyGroup group : groups) {
            ContentValues rowData = new ContentValues();
            rowData.put(GroupContract.COLUMN_NAME, group.getName());
            rowData.put(GroupContract.COLUMN_MEMBERS, group.getMembers());
            db.insert(GroupContract.TABLE_NAME, null, rowData);
        }
    }

    public ArrayList<MyGroup> getAllGroups() {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String[] projection = {GroupContract.COLUMN_NAME, GroupContract.COLUMN_MEMBERS};
        Cursor result = db.query(GroupContract.TABLE_NAME, projection, null, null, null, null, GroupContract.COLUMN_ID);
        ArrayList<MyGroup> groups = new ArrayList<MyGroup>();


        while (result.moveToNext()) {

            groups.add(new MyGroup(result.getString(result.getColumnIndex(GroupContract.COLUMN_NAME)),
                    result.getInt(result.getColumnIndex(GroupContract.COLUMN_MEMBERS))));
        }
        db.close();
        return groups;
    }

    public MyGroup getGroupById(int searchID) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();
        String queryStatement = "SELECT * FROM " + GroupContract.TABLE_NAME + " WHERE " + GroupContract.COLUMN_ID + "=" + searchID;
        Cursor result = db.rawQuery(queryStatement, null);
        MyGroup group = null;


        while (result.moveToNext()) {
            group = new MyGroup(result.getString(result.getColumnIndex(GroupContract.COLUMN_NAME)),
                    result.getInt(result.getColumnIndex(GroupContract.COLUMN_MEMBERS)));
        }
        db.close();
        return group;
    }

    public int deleteGroup(int groupId) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();
        String selection = GroupContract.COLUMN_ID + "=?";
        String[] args = {String.valueOf(groupId)};
        int deleteID = db.delete(GroupContract.TABLE_NAME, selection, args);
        return deleteID;
    }


    public int getGrouptId(MyGroup group) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String[] projection = {GroupContract.COLUMN_ID};
        String selections = GroupContract.COLUMN_NAME + "=?" + " AND " +
                GroupContract.COLUMN_MEMBERS + "=?";
        String[] args = {group.getName(), String.valueOf(group.getMembers())};
        Cursor result = db.query(GroupContract.TABLE_NAME, projection, selections, args, null, null, null);
        int id = result.getInt(result.getColumnIndex(GroupContract.COLUMN_ID));
        return id;
    }

    //All Relational data

    public ArrayList<MyContact> getAllContactsInTheSameGroup(int groupId) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();
        String queryStatement = "SELECT " + ContactContract.COLUMN_NAME + " , "
                + ContactContract.COLUMN_NUMBER
                + " FROM " + GroupMembersContract.TABLE_NAME + " INNER JOIN " + ContactContract.TABLE_NAME +
                " ON " + GroupMembersContract.COLUMN_CONTACT_ID + " = " + ContactContract.COLUMN_ID
                + " WHERE " + GroupMembersContract.COLUMN_GROUP_ID + "=" + groupId;
        Cursor result = db.rawQuery(queryStatement, null);
        ArrayList<MyContact> contacts = new ArrayList<>();

        while (result.moveToNext()) {
            contacts.add(new MyContact(result.getString(result.getColumnIndex(ContactContract.COLUMN_NAME)),
                    result.getString(result.getColumnIndex(ContactContract.COLUMN_NUMBER))));
        }
        return contacts;
    }

    public ArrayList<MyMessage> getAllMessagesInTheSameGroup(int groupId) {
        SQLiteDatabase db = mDpHelper.getReadableDatabase();

        String selection = GroupContract.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(groupId)};
        Cursor result = db.query(SmsContract.TABLE_NAME, null, selection, selectionArgs, null, null, "ASC");


        ArrayList<MyMessage> messages = new ArrayList<MyMessage>();


        while (result.moveToNext()) {

            messages.add(new MyMessage(result.getString(result.getColumnIndex(SmsContract.COLUMN_GROUP_ID)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_SMS_TEXT)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_DATE)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_TIME)),
                    result.getString(result.getColumnIndex(SmsContract.COLUMN_STATUS))));
        }
        db.close();
        return messages;
    }


    public int insertContactMessage(int contactId, int messageId) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer id = null;
        if (contactId != 0) {
            id = contactId;
        }
        values.put(ContactMessageContract.COLUMN_CONTACT_ID, id);
        values.put(ContactMessageContract.COLUMN_Message_ID, messageId);
        int id0 = (int) db.insert(ContactMessageContract.TABLE_NAME, null, values);
        return id0;
    }

    public void insertContactsIntoGroup(ArrayList<MyContact> contacts, int groupId) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        for (MyContact contact : contacts) {
            ContentValues rowData = new ContentValues();
            rowData.put(GroupMembersContract.COLUMN_CONTACT_ID, getContactId(contact));
            rowData.put(GroupMembersContract.COLUMN_GROUP_ID, groupId);
            db.insert(GroupMembersContract.TABLE_NAME, null, rowData);
        }
    }
}
