package com.elsafty.messagingmanager.SQLite;


public final class SmsContract {
    public static final String TABLE_NAME = "Messages";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_SMS_TEXT = "sms_text";
    public static final String COLUMN_STATUS = "status";


    public static final int STATUS_SCHEDULED = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;
    public static final int STATUS_TRASHED = 3;
}
