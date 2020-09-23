package com.elsafty.messagingmanager.Pojos;

public class MyTime {
    private int minute;
    private int hour;

    @Override
    public String toString() {
        return hour + " : " + minute;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
