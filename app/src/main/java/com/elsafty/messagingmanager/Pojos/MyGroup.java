package com.elsafty.messagingmanager.Pojos;

public class MyGroup {
    private String name;

    private int color = -1;
    private Integer members;
    private String photo ;

    public MyGroup(String name, Integer members) {
        this.name = name;
        this.members = members;
    }

    public String getPhoto() {
        return photo;
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
