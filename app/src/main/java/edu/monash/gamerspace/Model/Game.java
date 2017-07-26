/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: Game.java
Description: Contains the model of the game object of the application
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017
*/

//Implements all relevant libraries/packages

package edu.monash.gamerspace.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class Game implements Parcelable {
    // Database Constants
    public static final String TABLE_NAME = "game";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PUBLISHER = "publisher";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_AVAILABILITY = "availability";
    public static final String COLUMN_VENDOR = "vendor";
    // Table create statement
    public static final String CREATE_STATEMENT = "CREATE TABLE "
            + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_PUBLISHER + " TEXT NOT NULL, " +
            COLUMN_GENRE + " TEXT NOT NULL, " +
            COLUMN_DESC + " TEXT NOT NULL, " +
            COLUMN_PRICE + " INTEGER NOT NULL, " +
            COLUMN_AVAILABILITY + " TEXT NOT NULL, " +
            COLUMN_VENDOR + " TEXT NOT NULL " +
            ")";


    //Variables
    private long _id;
    private String m_sName;
    private String m_sPublisher;
    private String m_sGenre;
    private String m_sDesc;
    private int m_sPrice;
    private String m_sAvailability;
    private String m_sVendor;

    //Constructors
    public Game (){
        m_sName = "Nameless";
        m_sPublisher = "Typeless";
        m_sGenre = "Genreless";
        m_sDesc = " ";
        m_sPrice = 0;
        m_sAvailability = "Not available";
        m_sVendor = "None";
    }

    public Game(long id, String name, String publsher, String genre, String desc, int price, String avail, String where){
        this._id = id;
        this.m_sName = name;
        this.m_sPublisher = publsher;
        this.m_sGenre = genre;
        this.m_sDesc = desc;
        this.m_sPrice = price;
        this.m_sAvailability = avail;
        this.m_sVendor = where;
    }

    //Constructors for parsing information
    protected Game (Parcel in){
        _id = in.readLong();
        m_sName = in.readString();
        m_sPublisher = in.readString();
        m_sGenre = in.readString();
        m_sDesc = in.readString();
        m_sPrice = in.readInt();
        m_sAvailability = in.readString();
        m_sVendor = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeString(m_sName);
        parcel.writeString(m_sPublisher);
        parcel.writeString(m_sGenre);
        parcel.writeString(m_sDesc);
        parcel.writeInt(m_sPrice);
        parcel.writeString(m_sAvailability);
        parcel.writeString(m_sVendor);
    }


//Mutators and Accessors
    public long get_id() {return _id;}
    public void set_id(long id) {this._id = id;}

    public String getM_sName() {return m_sName;}
    public void setM_sName(String name){this.m_sName = name;}

    public String getM_sPublisher() {return m_sPublisher;}
    public void setM_sPublisher(String publisher){this.m_sPublisher = publisher;}

    public String getM_sGenre() {return m_sGenre;}
    public void setM_sGenre(String type){this.m_sGenre = type;}

    public String getM_sDesc() {return m_sDesc;}
    public void setM_sDesc(String desc){this.m_sDesc = desc;}

    public int getM_sPrice() {return m_sPrice;}
    public void setM_sPrice(int price) {this.m_sPrice = price;}

    public String getM_sAvailability() {return  m_sAvailability;}
    public void setM_sAvailability(String avail) {this.m_sAvailability = avail;}

    public String getM_sVendor() {return m_sVendor;};
    public void setM_sVendor(String vendor) {this.m_sVendor = vendor;}
}
