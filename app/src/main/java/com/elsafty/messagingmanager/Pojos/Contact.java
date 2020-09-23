package com.elsafty.messagingmanager.Pojos;

public class Contact {
    private String name;
    private String Number;
    private String imagePath;

    public Contact(String name, String number, String imagePath) {
        this.name = name;
        Number = number;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return Number;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", Number='" + Number + '\'' +
                '}';
    }
}
