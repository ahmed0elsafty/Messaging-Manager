package com.elsafty.messagingmanager.Pojos;

public class MyMessage {
    private String name;
    private String number;
    private String date;
    private String time;
    private String txtMessage;
    private String Status;

    public MyMessage(String name, String number, String date, String time, String txtMessage, String status) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.time = time;
        this.txtMessage = txtMessage;
        Status = status;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
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
        return "MyMessage{" +
                "txtMessage='" + txtMessage + '\'' +
                '}';
    }
}
