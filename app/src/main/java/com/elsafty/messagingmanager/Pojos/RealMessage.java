package com.elsafty.messagingmanager.Pojos;

public class RealMessage extends MyMessage{
    private String name;
    private String number;


    public RealMessage(String groupId, String txtMessage, String date, String time, String status,String name,String number) {
        super(groupId, txtMessage, date, time, status);
        this.name = name;
        this.number = number;
    }


    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
