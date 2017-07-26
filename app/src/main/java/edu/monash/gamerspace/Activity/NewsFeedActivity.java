/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: NewsFeedActivity.java
Description: Contains the activity for updating the news gathered from various RSS Feed for the app
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017
*/

//Implements all relevant libraries/packages
package edu.monash.gamerspace.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.monash.gamerspace.Adapter.RssFeedAdapter;
import edu.monash.gamerspace.R;
import edu.monash.gamerspace.Model.RssFeedModel;

public class NewsFeedActivity extends AppCompatActivity {
    //Variables needed for the activity

    //Navigation Drawer needed variables
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private RecyclerView mRecyclerView;

    //Swipe to refresh news
    private SwipeRefreshLayout mSwipeLayout;

    //News Integration needed variables
    private List<RssFeedModel> mFeedModelList;
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;
    private static final String TAG = "NewsFeedActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        setTitle("News");


        //Navigation drawer actions
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //==================================================================
        //==================================================================
        //==================================================================
        //==================================================================

        //News feed actions
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mEditText = (EditText) findViewById(R.id.rssFeedEditText);
//        mFetchFeedButton = (Button) findViewById(R.id.fetchFeedButton);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mFeedTitleTextView = (TextView) findViewById(R.id.feedTitle);
//        mFeedDescriptionTextView = (TextView) findViewById(R.id.feedDescription);
//        mFeedLinkTextView = (TextView) findViewById(R.id.feedLink);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new FetchFeedTask().execute((Void) null);

        //Swipe action trigger
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });
    }

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
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        Intent intent = new Intent(NewsFeedActivity.this, BrowseActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent2 = new Intent(NewsFeedActivity.this, BrowseVendorActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(NewsFeedActivity.this, AboutActivity.class);
                        startActivity(intent3);
                        break;
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
        int id = item.getItemId();
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * End of navigation bar code
    * */

    ///RSS FEED
    /*Courtesy of Rss Feed code:
    * Ogbo, O. (2016, December 9). Building a simple RSS reader – full tutorial with code | Android Authority.
    * Retrieved from Android Authority: http://www.androidauthority.com/simple-rss-reader-full-tutorial-733245/
    * */

    //Try to retrieve and parse the feeds into the recycler view
    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);


            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if(isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                    }
                    else {
                        mFeedTitle = title;
                        mFeedLink = link;
                        mFeedDescription = description;
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

//Responsible for the refreshing of the news feed and will be activated again if the user triggers the swipe to update gesture
    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
            mFeedTitle = null;
            mFeedLink = null;
            mFeedDescription = null;
//            mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
//            mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
//            mFeedLinkTextView.setText("Feed Link: " + mFeedLink);
//            urlLink = mEditText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
//            if (TextUtils.isEmpty(urlLink))
//                return false;

            try {
// The link which contains the aggregated Rss Feeds
                URL url = new URL("http://www.rssmix.com/u/8239053/rss.xml");
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        //Do these actions if the retrieval is successful
        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
//                mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
//                mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
//                mFeedLinkTextView.setText("Feed Link: " + mFeedLink);
                // Fill RecyclerView
                mRecyclerView.setAdapter(new RssFeedAdapter(mFeedModelList));
            } else {
                Toast.makeText(NewsFeedActivity.this,
                        "Nothing",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    /*End of Rss Feed Code*/

}
