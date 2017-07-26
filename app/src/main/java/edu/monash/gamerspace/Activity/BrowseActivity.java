/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: BrowseActivity.java
Description: Contains the activity for browsing through the games currently available in the vendors
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017
*/

//Implements all relevant libraries/packages

package edu.monash.gamerspace.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.monash.gamerspace.Adapter.GameAdapter;
import edu.monash.gamerspace.DatabaseHelper.DBHelper;
import edu.monash.gamerspace.Model.Game;
import edu.monash.gamerspace.R;



public class BrowseActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ValueEventListener {
 //Navigation drawer actions
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    private String mActivityTitle;

    //Public variable to check if the application has already contacted with the firebase database
    //So that it can set the database persistence once and not to be modified throughout the lifetime of the app
    public static boolean calledAlready = false;

    //Variables to show the contents
    private ListView m_cListView;
    private GameAdapter m_cAdapter;
    private ArrayList<Game> m_cGameList;
//    private DBHelper m_cDBHelper;

    //Firebase database variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        setTitle("Games");

        //Old SQLite Database
        // Get our database handler
//        m_cDBHelper = new DBHelper(getApplicationContext());
//        if(m_cDBHelper.GetAllGames().size() == 0) {
//            m_cDBHelper.CreateDefaultGames();
//        }



        //Navigation actions
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //====================================================================
        //====================================================================
        //====================================================================
        //====================================================================

//Firebase operations
        /*Firebase Code courtesy to Josh Olsen: Week 10 Lecture slides and others, which
        * will be mentioned in the abouts page
        * */
        //Instantiate the game arraylist
        m_cGameList = new ArrayList<>(
//                m_cDBHelper.GetAllGames().values()
        );

        //Call to firebase
        mDatabase = FirebaseDatabase.getInstance();

        //Check if firebase has already called, if not, then set the persistence of the database
        if (!calledAlready){
            mDatabase.setPersistenceEnabled(true);
            calledAlready = true;
        }
        //Get the databse reference (as there will be two separate references)
        mDatabaseRef = mDatabase.getReference("Games");

    //    InitialiseFirebaseObjects();
        m_cListView = (ListView) findViewById(R.id.prodListView);

        // Create Adapter and associate it with the Game item
        m_cAdapter = new GameAdapter(this, m_cGameList);
        m_cListView.setAdapter(m_cAdapter);
        m_cListView.setOnItemClickListener(this);
    }

    //On click listener, go to each game's details
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent newIntent = new Intent(this, ViewGameActivity.class);
        newIntent.putExtra("GAME", m_cGameList.get(i));
        startActivity(newIntent);
    }
    //====================================================================
    //====================================================================
    //====================================================================
    //====================================================================


    //Firebase operations
    private void RefreshListView() {
        m_cAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mDatabaseRef != null)
            mDatabaseRef.addValueEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if(mDatabaseRef != null)
            mDatabaseRef.removeEventListener(this);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }

    public void onDataChange(DataSnapshot dataSnapshot) {

        m_cGameList.clear();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Game newGame = snapshot.getValue(Game.class);
            m_cGameList.add(newGame);
        }
        RefreshListView();
    }

    //====================================================================
    //====================================================================
    //====================================================================
    //====================================================================




    //NAVIGATION BAR NEEDED CODES
    /*
    * Courtesy of navigation bar code:
    *
    * Jakuben, B. (2015, February 2). How to Add a Navigation Drawer in Android.
    * Retrieved from Treehouse: http://blog.teamtreehouse.com/add-navigation-drawer-android

    * */
//Add menu items to the drawer bar
    private void addDrawerItems() {
        String[] osArray = { "News", "Browse Games", "Our vendors", "About" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //First item
                        Intent intent = new Intent(BrowseActivity.this, NewsFeedActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        Intent intent2 = new Intent(BrowseActivity.this, BrowseVendorActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(BrowseActivity.this, AboutActivity.class);
                        startActivity(intent3);
                    default:
                        break;
                }
            }
        });
    }
    //Set up navigation drawer
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Initialise objects and put them into the firebase database
    public void InitialiseFirebaseObjects(){
        Game game1 = new Game(1,
                "Metal Gear Solid: The Phantom Pain",
                "Konami",
                "Action",
                "METAL GEAR SOLID V: The Phantom Pain, will provide players a first-rate gaming experience as they are offered tactical freedom to carry out open-world missions",
                20,
                "Available",
                "GameSpace HQ");
        Game game2 = new Game(2,
                "Rise of the Tomb Raider™",
                "Square Enix",
                "Adventure",
                "Rise of the Tomb Raider: 20 Year Celebration includes the base game and Season Pass featuring all-new content.",
                50,
                "Available",
                "GameSpace HQ");
        Game game3 = new Game(3,
                "Downwell",
                "Devolver Digital",
                "Action",
                "Downwell is a curious game about a young person venturing down a well in search of untold treasures with only his Gunboots to protect him.",
                1,
                "Available",
                "GameSpace HQ");
        Game game4 = new Game(4,
                "World of Warcraft",
                "Blizzard Ent.",
                "MMO",
                "Descend into World of Warcraft and join millions of mighty heroes in an online world of myth, magic and limitless adventure. An infinity of experiences await.",
                60,
                "Out of stock",
                "Blizzard Dealer");
        Game game5 = new Game(5,
                "Portal 2",
                "Valve",
                "Puzzle",
                "Portal 2 draws from the award-winning formula of innovative gameplay, story, and music that earned the original Portal over 70 industry accolades and created a cult following.",
                20,
                "Available",
                "Gabe's Corner");
        Game game6 = new Game(6,
                "Dark Souls III",
                "FromSoftware, Inc., BANDAI NAMCO ",
                "RPG",
                "Dark Souls continues to push the boundaries with the latest, ambitious chapter in the critically-acclaimed and genre-defining series. Prepare yourself and Embrace The Darkness!",
                60,
                "Available",
                "GameSpace HQ");
        Game game7 = new Game(7,
                "No Man's Sky",
                "Konami",
                "Sci-fi",
                "No Man's Sky is a game about exploration and survival in an infinite procedurally generated universe.",
                60,
                "Out of stock",
                "GameSpace HQ");
        Game game8 = new Game(8,
                "Overwatche",
                "Blizzard Ent.",
                "Shooter",
                "Conflict is rising across the world again, and the call has gone out to heroes old and new. Are you with Overwatch?",
                20,
                "Available",
                "Blizzard Dealer");
        Game game9 = new Game(9,
                "Rocket League®",
                "Psyonix, Inc.",
                "Sport",
                "Soccer meets driving once again in the long-awaited, physics-based multiplayer-focused sequel to Supersonic Acrobatic Rocket-Powered Battle-Cars! ",
                20,
                "Available",
                "GameSpace HQ");
        Game game10 = new Game(10,
                "Age of Empires II HD",
                "Microsoft Studios",
                "Strategy",
                "Age of Empires II has been re-imagined in high definition with new features, trading cards, improved AI, workshop support, multiplayer, Steamworks integration and more!",
                40,
                "Available",
                "GameSpace HQ");
        mDatabaseRef.child(game1.get_id() + " " + game1.getM_sName()).setValue(game1);
        m_cGameList.add(game1);
        mDatabaseRef.child(game2.get_id() +  " " + game2.getM_sName()).setValue(game2);
        m_cGameList.add(game2);
        mDatabaseRef.child(game3.get_id() + " " +  game3.getM_sName()).setValue(game3);
        m_cGameList.add(game3);
        mDatabaseRef.child(game4.get_id() +  " " + game4.getM_sName()).setValue(game4);
        m_cGameList.add(game4);
        mDatabaseRef.child(game5.get_id() +  " " + game5.getM_sName()).setValue(game5);
        m_cGameList.add(game5);
        mDatabaseRef.child(game6.get_id() + " " +  game6.getM_sName()).setValue(game6);
        m_cGameList.add(game6);
        mDatabaseRef.child(game7.get_id() +  " " + game7.getM_sName()).setValue(game7);
        m_cGameList.add(game7);
        mDatabaseRef.child(game8.get_id() +  " " + game8.getM_sName()).setValue(game8);
        m_cGameList.add(game8);
        mDatabaseRef.child(game9.get_id() + " " +  game9.getM_sName()).setValue(game9);
        m_cGameList.add(game9);
        mDatabaseRef.child(game10.get_id() +  " " + game10.getM_sName()).setValue(game10);
        m_cGameList.add(game10);



        Toast.makeText(BrowseActivity.this,
                "All games been added!",
                Toast.LENGTH_SHORT).show();
    }
}
