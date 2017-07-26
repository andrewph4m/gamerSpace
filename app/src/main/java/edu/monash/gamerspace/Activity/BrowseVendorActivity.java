/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: BrowseVendorActivity.java
Description: Contains the activity for browsing the available vendors in the application
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.monash.gamerspace.Adapter.GameAdapter;
import edu.monash.gamerspace.Adapter.VendorAdapter;
import edu.monash.gamerspace.Model.Game;
import edu.monash.gamerspace.Model.Vendor;
import edu.monash.gamerspace.R;

import static edu.monash.gamerspace.Activity.BrowseActivity.calledAlready;

public class BrowseVendorActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ValueEventListener {

    //Navigation bar variables
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;


    //Variables for showing all the contents
    private ListView m_cVendorListView;
    private VendorAdapter m_cVendorAdapter;
    private ArrayList<Vendor> m_cVendorList;

    //firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_vendor);

        setTitle("Vendors");

        //Navigation Bar actions
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
        m_cVendorList = new ArrayList<>(
        );

        mDatabase = FirebaseDatabase.getInstance();

        if (!calledAlready){
            mDatabase.setPersistenceEnabled(true);
            calledAlready = true;
        }
        //Get the databse reference (as there will be two separate references)
        mDatabaseRef = mDatabase.getReference("Vendors");

        //InitialiseVendorFirebaseObjects();
        m_cVendorListView = (ListView) findViewById(R.id.vendorListView);

        // Create Adapter and associate it with our Product
        m_cVendorAdapter = new VendorAdapter(this, m_cVendorList);
        m_cVendorListView.setAdapter(m_cVendorAdapter);
        m_cVendorListView.setOnItemClickListener(this);
    }

    //On click listener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent newIntent = new Intent(this, ContactActivity.class);
        newIntent.putExtra("VENDOR", m_cVendorList.get(i));
        startActivity(newIntent);
    }

    //Firebase operations
    private void RefreshListView() {
        m_cVendorAdapter.notifyDataSetChanged();
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

        m_cVendorList.clear();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Vendor newVendor = snapshot.getValue(Vendor.class);
            m_cVendorList.add(newVendor);
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
    private void addDrawerItems() {
        String[] osArray = { "News", "Browse Games", "Our vendors", "About" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //First item
                        Intent intent = new Intent(BrowseVendorActivity.this, NewsFeedActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(BrowseVendorActivity.this, BrowseActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        mDrawerLayout.closeDrawers();
                        break;
                    case 3:
                        Intent intent4 = new Intent(BrowseVendorActivity.this, AboutActivity.class);
                        startActivity(intent4);
                        break;
                    default:
                        break;
                }
            }
        });
    }

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

    /*End of navigation bar codes*/

    public void InitialiseVendorFirebaseObjects(){
        Vendor gamespace = new Vendor(1, "Gamespace HQ", "67 Swanston St, Melbourne VIC 3000, Australia",-37.816015,144.966526, "The Gamerspace HQ. We have lots of games - Opens 8AM to 10PM");
        Vendor blizzard = new Vendor(2, "Blizzard Deals", "67 Swanston St, Melbourne VIC 3000, Australia",-37.813162, 144.970430, "The place for Blizzard Deals - Opens 10AM - 8PM");
        Vendor gaben = new Vendor(3, "Gabe's Corner", "8 Atherton Rd, Oakleigh VIC 3166, Australia",-37.898631,145.088201, "All the good deals here at Gabe's Corner - Opens 9AM - 7PM");

        mDatabaseRef.child(gamespace.get_id() + " " + gamespace.getM_sName()).setValue(gamespace);
        m_cVendorList.add(gamespace);
        mDatabaseRef.child(blizzard.get_id() +  " " + blizzard.getM_sName()).setValue(blizzard);
        m_cVendorList.add(blizzard);
        mDatabaseRef.child(gaben.get_id() + " " +  gaben.getM_sName()).setValue(gaben);
        m_cVendorList.add(gaben);
    }

}
