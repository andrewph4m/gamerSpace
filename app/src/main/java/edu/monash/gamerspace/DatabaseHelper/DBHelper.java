/*

Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: DBHelper.java
Description: Contains the code which creates the SQLite when the app was first locally stored on the phone
DISCLAIMER: THIS CLASS IS NOT USED ANY MORE, KEEP THIS FOR REFERENCE
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017
*/

//Implements all relevant libraries/packages

package edu.monash.gamerspace.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

import edu.monash.gamerspace.Model.Game;

/**
 * Created by MSI on 21-May-17.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "gameDB";
    public static final int DATABASE_VERSION = 1;

    //Constructor of the DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //Create and upgrade relevant actions
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Game.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Game.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //Method to add a game object into the local database
    public void AddGame(Game Game) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Game.COLUMN_NAME, Game.getM_sName());
        values.put(Game.COLUMN_PUBLISHER, Game.getM_sPublisher());
        values.put(Game.COLUMN_GENRE, Game.getM_sGenre());
        values.put(Game.COLUMN_DESC, Game.getM_sDesc());
        values.put(Game.COLUMN_PRICE, Game.getM_sPrice());
        values.put(Game.COLUMN_AVAILABILITY, Game.getM_sAvailability());
        values.put(Game.COLUMN_VENDOR, Game.getM_sVendor());
        db.insert(Game.TABLE_NAME, null, values);
        db.close();
    }

//Get game details in HashMap form
    public HashMap<Long, Game> GetAllGames() {
        HashMap<Long, Game> Games = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Game.TABLE_NAME, null);

        // Add each person to hashmap (Each row has 1 person)
        if(cursor.moveToFirst()) {
            do {
                Game Game = new Game(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getString(7));
                Games.put(Game.get_id(), Game);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return Games;
    }

    //Delete a game
    public void RemoveGame(Game Game)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Game.TABLE_NAME,
                Game.COLUMN_ID + " = ?",
                new String[] {String.valueOf(Game.get_id())});
    }

    //Create dummy data for the database
    public void CreateDefaultGames(){
        AddGame(new Game(1,
                "Metal Gear Solid: The Phantom Pain",
                "Konami",
                "Action",
                "METAL GEAR SOLID V: The Phantom Pain, will provide players a first-rate gaming experience as they are offered tactical freedom to carry out open-world missions",
                20,
                "Available",
                "GameSpace HQ"));
        AddGame(new Game(2,
                "Rise of the Tomb Raider™",
                "Square Enix",
                "Adventure",
                "Rise of the Tomb Raider: 20 Year Celebration includes the base game and Season Pass featuring all-new content.",
                50,
                "Available",
                "GameSpace HQ"));
        AddGame(new Game(3,
                "Downwell",
                "Devolver Digital",
                "Action",
                "Downwell is a curious game about a young person venturing down a well in search of untold treasures with only his Gunboots to protect him.",
                1,
                "Available",
                "GameSpace HQ"));
        AddGame(new Game(4,
                "World of Warcraft",
                "Blizzard Ent.",
                "MMO",
                "Descend into World of Warcraft and join millions of mighty heroes in an online world of myth, magic and limitless adventure. An infinity of experiences await.",
                60,
                "Out of stock",
                "Blizzard Dealer"));
        AddGame(new Game(5,
                "Portal 2",
                "Valve",
                "Puzzle",
                "Portal 2 draws from the award-winning formula of innovative gameplay, story, and music that earned the original Portal over 70 industry accolades and created a cult following.",
                20,
                "Available",
                "Gabe's Corner"));
        AddGame(new Game(6,
                "Dark Souls III",
                "FromSoftware, Inc., BANDAI NAMCO ",
                "RPG",
                "Dark Souls continues to push the boundaries with the latest, ambitious chapter in the critically-acclaimed and genre-defining series. Prepare yourself and Embrace The Darkness!",
                60,
                "Available",
                "GameSpace HQ"));
        AddGame(new Game(7,
                "No Man's Sky",
                "Konami",
                "Sci-fi",
                "No Man's Sky is a game about exploration and survival in an infinite procedurally generated universe.",
                60,
                "Out of stock",
                "GameSpace HQ"));
        AddGame(new Game(8,
                "Overwatche",
                "Blizzard Ent.",
                "Shooter",
                "Conflict is rising across the world again, and the call has gone out to heroes old and new. Are you with Overwatch?",
                20,
                "Available",
                "Blizzard Dealer"));
        AddGame(new Game(9,
                "Rocket League®",
                "Psyonix, Inc.",
                "Sport",
                "Soccer meets driving once again in the long-awaited, physics-based multiplayer-focused sequel to Supersonic Acrobatic Rocket-Powered Battle-Cars! ",
                20,
                "Available",
                "GameSpace HQ"));
        AddGame(new Game(10,
                "Age of Empires II HD",
                "Microsoft Studios",
                "Strategy",
                "Age of Empires II has been re-imagined in high definition with new features, trading cards, improved AI, workshop support, multiplayer, Steamworks integration and more!",
                40,
                "Available",
                "GameSpace HQ"));
    }
}
