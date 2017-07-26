/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: Vendor.java
Description: Contains the information relates to the Vendor model in the application
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017
*/

//Implements all relevant libraries/packages
package edu.monash.gamerspace.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class Vendor implements Parcelable{

    //Variables
    private long _id;
    private String m_sName;
    private String m_sAddress;
    private double m_sLatitude;
    private double m_sLongitude;
    private String m_sDesc;

    public Vendor(){
        m_sName = "Nameless";
        m_sAddress = "Addressless";
        m_sLatitude = 0;
        m_sLongitude = 0;
        m_sDesc = "";
    }

    public Vendor(long id, String name, String address, double latitude, double longitude, String desc){
        this._id = id;
        this.m_sName = name;
        this.m_sAddress = address;
        this.m_sLatitude = latitude;
        this.m_sLongitude = longitude;
        this.m_sDesc = desc;
    }


    //Constructors for parsing information
    protected Vendor (Parcel in){
        _id = in.readLong();
        m_sName = in.readString();
        m_sAddress = in.readString();
        m_sLatitude = in.readDouble();
        m_sLongitude = in.readDouble();
        m_sDesc = in.readString();
    }

    public static final Parcelable.Creator<Vendor> CREATOR = new Parcelable.Creator<Vendor>() {
        @Override
        public Vendor createFromParcel(Parcel in) {
            return new Vendor(in);
        }

        @Override
        public Vendor[] newArray(int size) {
            return new Vendor[size];
        }
    };


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeString(m_sName);
        parcel.writeString(m_sAddress);
        parcel.writeDouble(m_sLatitude);
        parcel.writeDouble(m_sLongitude);
        parcel.writeString(m_sDesc);
    }

    //Mutators and Accessors
    public long get_id() {return _id;}
    public void set_id(long id) {this._id = id;}

    public String getM_sName() {return m_sName;}
    public void setM_sName(String name){this.m_sName = name;}

    public String getM_sAddress() {return m_sAddress;}
    public void setM_sAddress(String address){this.m_sAddress = address;}

    public double getM_sLatitude() {return m_sLatitude;}
    public void setM_sGenre(double latitude){this.m_sLatitude = latitude;}

    public double getM_sLongitude() {return m_sLongitude;}
    public void setM_sLongitude(double longitude){this.m_sLongitude = longitude;}

    public String getM_sDesc() {return m_sDesc;}
    public void setM_sDesc(String desc){this.m_sDesc = desc;}
}
