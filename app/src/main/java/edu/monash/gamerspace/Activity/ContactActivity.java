/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: ContactActivity.java
Description: Contains the details of the vendors
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017
*/

//Implements all relevant libraries/packages

package edu.monash.gamerspace.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.monash.gamerspace.Model.Game;
import edu.monash.gamerspace.Model.Vendor;
import edu.monash.gamerspace.R;

import static edu.monash.gamerspace.R.id.vdrImageView;

public class ContactActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Variables for storing the text views
    private TextView vdrNameText;
    private TextView vdrDescText;
    private TextView vdrAddrText;
    private ImageView vendorImageView;

    // Min and Max Update Intervals for Location Service
    private static final long MAX_UPDATE_INTERVAL = 10000; // 10 Seconds
    private static final long MIN_UPDATE_INTERVAL = 2000;  // 2 Seconds

    //Variables for google maps
    private GoogleMap m_cGoogleMap;
    private Location m_cCurrentLocation;
    private GoogleApiClient m_cAPIClient;

    private StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //Get the details for the text view elements in the activity
        vdrNameText = (TextView) findViewById(R.id.vdrNameTextView);
        vdrAddrText = (TextView) findViewById(R.id.vdrAddrTextView);
        vdrDescText = (TextView) findViewById(R.id.vdrDescTextView);
        vendorImageView = (ImageView) findViewById(vdrImageView);

        //Navigation actions (back to the previous page)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Get previous intent and have them display the details on the interface
        Intent prevIntent = getIntent();
        if(prevIntent != null) {

            try {
                Vendor currentVendor = prevIntent.getParcelableExtra("VENDOR");
                vdrNameText.setText(currentVendor.getM_sName());
                vdrAddrText.setText(currentVendor.getM_sAddress());
                vdrDescText.setText(currentVendor.getM_sDesc());

                mStorageReference = FirebaseStorage.getInstance().getReference("Vendors/" + currentVendor.get_id() + ".png");
                mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();

                        Glide.with(ContactActivity.this)
                                .load(url)
                                .into(vendorImageView)
                        ;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }

        // Check to see if our APIClient is null.
        if(m_cAPIClient == null) {
            // Create API Client and tell it to connect to Location Services
            m_cAPIClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Get access to  MapFragment
        MapFragment mapFrag = (MapFragment)
                getFragmentManager().findFragmentById(R.id.map_fragment);
        // Set up an asyncronous callback to let us know when the map has loaded
        mapFrag.getMapAsync(this);

    }


    //Google map codes
    /*
    * Courtesy of Josh Olsen: Android Week 6 Tutorial Instruction
    * */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Function is called once the map has fully loaded.
        // Set our map object to reference the loaded map
        m_cGoogleMap = googleMap;

        //Get previous intent and have them display the coordinates of the vendors to the map
        Intent prevIntent = getIntent();
        if(prevIntent != null) {
            try {
                Vendor currentVendor = prevIntent.getParcelableExtra("VENDOR");

                final LatLng LOCATION_CURR_VENDOR = new LatLng(currentVendor.getM_sLatitude(), currentVendor.getM_sLongitude());

                m_cGoogleMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(LOCATION_CURR_VENDOR, 20));
                //Set marker name to vendor name
                m_cGoogleMap.addMarker(new MarkerOptions().position(LOCATION_CURR_VENDOR).title(currentVendor.getM_sName()));
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // Android 6.0 & up added security permissions
        // If the user rejects allowing access to location data then this try block
        // will stop the application from crashing (Will not track location)
        try {
            // Set up a constant updater for location services
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(MAX_UPDATE_INTERVAL)
                    .setFastestInterval(MIN_UPDATE_INTERVAL);

            LocationServices.FusedLocationApi
                    .requestLocationUpdates(m_cAPIClient, locationRequest, this);
        }
        catch (SecurityException secEx) {
            Toast.makeText(this, "ERROR: Please enable location services",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        // Do nothing for now.
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Do nothing for now.
    }

    @Override
    public void onLocationChanged(Location location) {
        // This is our function that is called whenever we change locations
        // Update our current location variable
        m_cCurrentLocation = location;
        //ChangeMapLocation();
    }

    private void ChangeMapLocation() {
        // Check to ensure map and location are not null
        if(m_cCurrentLocation != null && m_cGoogleMap != null) {
            // Create a new LatLng based on our new location
            LatLng newPos = new LatLng(m_cCurrentLocation.getLatitude(),
                    m_cCurrentLocation.getLongitude());
            // Change the map focus to be our new location
            m_cGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPos, 15));
        }
    }

    @Override
    protected void onStart() {
        m_cAPIClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        m_cAPIClient.disconnect();
        super.onStop();
    }

    //Go back to the previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), BrowseVendorActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
