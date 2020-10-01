package com.elsafty.messagingmanager.SQLite;


public final class SmsContract {
    public static final String TABLE_NAME = "Messages";
    public static final String COLUMN_ID = "Messages_id";
    public static final String COLUMN_GROUP_ID = "Messages_group_id";
    public static final String COLUMN_DATE = "Messages_date";
    public static final String COLUMN_TIME = "Messages_time";
    public static final String COLUMN_SMS_TEXT = "Messages_sms_text";
    public static final String COLUMN_STATUS = "Messages_status";


    public static final int STATUS_SCHEDULED = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;
    public static final int STATUS_TRASHED = 3;

}
