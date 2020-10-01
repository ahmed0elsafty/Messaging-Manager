package com.elsafty.messagingmanager.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.elsafty.messagingmanager.R;

public class MyContact implements Parcelable {
    private String name;
    private String Number;
    public static final Creator<MyContact> CREATOR = new Creator<MyContact>() {
        @Override
        public MyContact createFromParcel(Parcel in) {
            return new MyContact(in);
        }

        @Override
        public MyContact[] newArray(int size) {
            return new MyContact[size];
        }
    };
    private int color = R.color.red_500;
    private Integer image = null;

    protected MyContact(Parcel in) {
        name = in.readString();
        Number = in.readString();
        color = in.readInt();
        if (in.readByte() == 0) {
            image = null;
        } else {
            image = in.readInt();
        }
    }

    public MyContact(String name, String number) {
        this.name = name;
        Number = number;

    }

    public Integer getImage() {
        return image;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return Number;
    }

    @Override
    public String toString() {
        return "MyContact{" +
                "name='" + name + '\'' +
                ", Number='" + Number + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(Number);
        parcel.writeInt(color);
        if (image == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(image);
        }
    }
}
