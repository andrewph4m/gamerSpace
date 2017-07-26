/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: AboutActivity.java
Description: Contains the sources, external libraries, packages and citations that the application uses
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
import android.widget.TextView;

import edu.monash.gamerspace.R;

public class AboutActivity extends AppCompatActivity {

    //Variables for the navigation drawer
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private TextView aboutTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle("About this app");



        aboutTextView = (TextView) findViewById(R.id.abtTextView);

        aboutTextView.setText("The App would like to thank all of these people that have helped the app to develop.\n" +
                "Surely the app is expected to be better than this and promise there will be improvements in the future\n" +
                "Sources that helped the app grows include:\n" +
                "Alboteanu, D. (2016, May 31). setPersistenceEnabled(true) crashes app - Stack Overflow. Retrieved from Stack Overflow: https://stackoverflow.com/questions/37448186/setpersistenceenabledtrue-crashes-app\n" +
                "GameSpot. (n.d.). GameSpot: Video Games Reviews & News. Retrieved from https://www.gamespot.com/feeds/mashup/\n" +
                "Google Inc. (2017, June 5). firebase/FirebaseUI-Android: UI Binding for Firebase. Retrieved from GitHub: https://github.com/firebase/FirebaseUI-Android\n" +
                "IGN Entertainment. (n.d.). IGN Entertainment. Retrieved from http://feeds.ign.com/ign/games-all\n" +
                "ioan ghip. (2014, February 13). Change the project theme in Android Studio? - Stack OverFlow. Retrieved from Stack OverFlow: https://stackoverflow.com/questions/21762203/change-the-project-theme-in-android-studio\n" +
                "Jakuben, B. (2015, February 2). How to Add a Navigation Drawer in Android. Retrieved from Treehouse: http://blog.teamtreehouse.com/add-navigation-drawer-android\n" +
                "Jeff. (2016, July 25). How do I open specific activities when selecting from an array in a navigation drawer? - Stack Overflow. Retrieved from Stack Overflow: https://stackoverflow.com/questions/38558620/how-do-i-open-specific-activities-when-selecting-from-an-array-in-a-navigation-d\n" +
                "Khan, B. (2017, February 28). Firebase Realtime Database CRUD Operation for Android. Retrieved from Simplified Coding: https://www.simplifiedcoding.net/firebase-realtime-database-crud/\n" +
                "Metacritic. (n.d.). Retrieved from Metacritic: http://www.metacritic.com/rss/features\n" +
                "Miller, J. C. (n.d.). Gaming Icon Set. Retrieved from Hectick: http://www.hectick.net/gaming-icon-set.html\n" +
                "Ng, K. (2017, March 12). Android Firebase Realtime Database CRUD Tutorial - QuestDot. Retrieved from QuestDot: https://questdot.com/android-firebase-realtime-database-crud-tutorial/\n" +
                "Ogbo, O. (2016, December 9). Building a simple RSS reader – full tutorial with code | Android Authority. Retrieved from Android Authority: http://www.androidauthority.com/simple-rss-reader-full-tutorial-733245/\n" +
                "Orlowski, M. (2016, January 3). How do I make links in a TextView clickable? - Stack Overflow. Retrieved from Stack Overflow: https://stackoverflow.com/questions/2734270/how-do-i-make-links-in-a-textview-clickable\n" +
                "Olsen, J. (2017). Advanced Android Functionality. Retrieved from FIT3027 - Android and iOS development: http://moodle.vle.monash.edu/mod/resource/view.php?id=4038540\n" +
                "Olsen, J. (2017). Maps and Geolocation. Retrieved from FIT3027 - Android and iOS Development: http://moodle.vle.monash.edu/mod/resource/view.php?id=3852810\n"+
                "Peitek, N. (2015, October 1). Glide — ListAdapter. Retrieved from Future Studio: https://futurestud.io/tutorials/glide-listadapter-listview-gridview\n" +
                "PlanetAstro_William. (2016, March 6). Large gap forms between RecyclerView items when scrolling down - Stack Overflow. Retrieved from Stack Overflow: https://stackoverflow.com/questions/35817610/large-gap-forms-between-recyclerview-items-when-scrolling-down\n" +
                "RssMix. (n.d.). RSS Aggregator. Retrieved from http://www.rssmix.com/\n" +
                "Vipul, J. (2013, October 5). How to close navigation drawer when an item is pressed from it? - Stack Overflow. Retrieved from Stack Overflow: https://stackoverflow.com/questions/19194526/how-to-close-navigation-drawer-when-an-item-is-pressed-from-it\n" +
                "Yadav, D. (2016, February 20). Android App is crashing when connecting to Firebase - Stack Overflow. Retrieved from Stack Overflow: https://stackoverflow.com/questions/35522717/android-app-is-crashing-when-connecting-to-firebase\n" +
                "\n" +
                " © 2015 Google Inc. All rights reserved. Google Maps and Firebase is a trademark of Google Inc.");

        //Navigation drawer actions
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

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
                        Intent intent = new Intent(AboutActivity.this, NewsFeedActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(AboutActivity.this, BrowseActivity.class);
                        startActivity(intent2);
                    case 2:
                        Intent intent3 = new Intent(AboutActivity.this, BrowseVendorActivity.class);
                        startActivity(intent3);
                    case 3:
                        mDrawerLayout.closeDrawers();
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

    /*End of navigation bar code
    * */
}
