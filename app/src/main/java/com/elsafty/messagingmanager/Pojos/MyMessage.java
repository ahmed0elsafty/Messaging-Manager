package com.elsafty.messagingmanager.Pojos;

public class MyMessage {
    private String groupId;
    private String txtMessage;
    private String date;
    private String time;
    private String Status;

    public MyMessage(String groupId, String txtMessage, String date, String time, String status) {
        this.groupId = groupId;
        this.txtMessage = txtMessage;
        this.date = date;
        this.time = time;

        Status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTxtMessage() {
        return txtMessage;
    }

    public String getStatus() {
        return Status;
    }


    @Override
    public String toString() {
        return txtMessage;
    }
}
