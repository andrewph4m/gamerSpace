/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: ViewGameActivity.java
Description: Display the details of the chosen game from the browse game activity
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
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.monash.gamerspace.Model.Game;
import edu.monash.gamerspace.R;

public class ViewGameActivity extends AppCompatActivity {

    //Variables for the display of the details of the game

    private TextView publisherTextView;
    private TextView genreTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView availTextView;
    private TextView vendorTextView;
    private ImageView gameImageView;


    //Firebase Storage reference for retrieving pictures
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);

        //Set up back button to go back to the previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Get the elements in the interface and have them bind to the variables
        publisherTextView = (TextView) findViewById(R.id.gamePubTextView);
        genreTextView = (TextView) findViewById(R.id.gameGenreTextView);
        descriptionTextView = (TextView) findViewById(R.id.descTextView);
        priceTextView = (TextView) findViewById(R.id.gamePriceTextView);
        availTextView = (TextView) findViewById(R.id.gameAvailTextView);
        vendorTextView = (TextView) findViewById(R.id.gameVendorTextView);
        gameImageView = (ImageView) findViewById(R.id.gameImgView);

        // Get the intent if it exists
        Intent prevIntent = getIntent();

        if(prevIntent != null) {
            // Try and get the game object
            try {
                Game currentGame = prevIntent.getParcelableExtra("GAME");

                //Set the elements corresponds to the game details
                setTitle(currentGame.getM_sName());
                publisherTextView.setText(currentGame.getM_sPublisher());
                genreTextView.setText(currentGame.getM_sGenre());
                descriptionTextView.setText(currentGame.getM_sDesc());
                priceTextView.setText("$" + currentGame.getM_sPrice());
                availTextView.setText(currentGame.getM_sAvailability());
                vendorTextView.setText("Sold at " + currentGame.getM_sVendor());

                String avail = currentGame.getM_sAvailability();
                //Format the colour based on the availability
                if(avail.equalsIgnoreCase("available")){
                    availTextView.setTextColor(Color.parseColor("#299104"));
                } else {
                    availTextView.setTextColor(Color.parseColor("#b70505"));
                }

                //Change the image view based on the genre
                /*String genre = currentGame.getM_sGenre();
                if (genre.equalsIgnoreCase("action")) {
                    gameImageView.setImageResource(R.mipmap.action);
                } else if (genre.equalsIgnoreCase("adventure")) {
                    gameImageView.setImageResource(R.mipmap.adventure);
                } else if (genre.equalsIgnoreCase("arcade")) {
                    gameImageView.setImageResource(R.mipmap.arcade);
                } else if (genre.equalsIgnoreCase("mmo")) {
                    gameImageView.setImageResource(R.mipmap.mmo);
                } else if (genre.equalsIgnoreCase("puzzle")) {
                    gameImageView.setImageResource(R.mipmap.puzzle);
                } else if (genre.equalsIgnoreCase("rpg")) {
                    gameImageView.setImageResource(R.mipmap.rpg);
                } else if (genre.equalsIgnoreCase("sci-fi")) {
                    gameImageView.setImageResource(R.mipmap.scifi);
                } else if (genre.equalsIgnoreCase("shooter")) {
                    gameImageView.setImageResource(R.mipmap.shooter);
                } else if (genre.equalsIgnoreCase("sport")) {
                    gameImageView.setImageResource(R.mipmap.sport);
                } else if (genre.equalsIgnoreCase("strategy")) {
                    gameImageView.setImageResource(R.mipmap.strategy);
                }*/

                //Access the firebase storage to get the picture (the old way)
//                mStorageReference = FirebaseStorage.getInstance().getReference("Games/Posters/" + currentGame.get_id() + ".png");
//                mStorageReference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//                        Bitmap newMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        gameImageView.setImageBitmap(newMap);
//                    }
//                });

                //Access the firebase storage to get the picture (the new way)
                //Using GLIDE to access, download and cache the picture in the device
                mStorageReference = FirebaseStorage.getInstance().getReference("Games/Icons/" + currentGame.get_id() + ".png");
                mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        //Load the picture from the firebase address and put it into the imageview element
                        Glide.with(ViewGameActivity.this)
                                .load(url)
                                .into(gameImageView)
                        ;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle errors. None for now
                    }
                });
            } catch(Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }


    }

    @Override
    //Go back to the previous activity
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), BrowseActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

}
