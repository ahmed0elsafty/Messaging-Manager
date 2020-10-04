package com.elsafty.messagingmanager.Pojos;

import com.elsafty.messagingmanager.R;

public class MyGroup {
    private String name;

    private int color = R.color.red_500;
    private Integer members;


    public MyGroup(String name, Integer members) {
        this.name = name;
        this.members = members;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

}
