package com.elsafty.messagingmanager.Pojos;

public class RealMessage extends MyMessage{
    private String name;


    public RealMessage(String groupId, String txtMessage, String date, String time, String status,String name) {
        super(groupId, txtMessage, date, time, status);
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
