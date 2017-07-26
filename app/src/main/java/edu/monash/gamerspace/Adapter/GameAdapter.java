/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: GameAdapter.java
Description: Contains the adapter for the games' list view items
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017

*/

//Implements all relevant libraries/packages
package edu.monash.gamerspace.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


import edu.monash.gamerspace.Model.Game;
import edu.monash.gamerspace.R;

import static java.security.AccessController.getContext;

//Game Adapter
public class GameAdapter extends BaseAdapter {
    private Context m_cCurrentContext;
    private ArrayList<Game> m_cGameList;

    private StorageReference mStorageReference;


    //Adapter constructor
    public GameAdapter(Context con, ArrayList<Game> games) {
        m_cCurrentContext = con;
        m_cGameList = games;
    }

    @Override
    public int getCount() {
        return m_cGameList.size();
    }

    @Override
    public Object getItem(int i) {
        return m_cGameList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {

        // Check if view already exists. If not inflate it
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) m_cCurrentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Create a list item based on the list item
            view = inflater.inflate(R.layout.list_game_item, null);
        }

        //Set the elements in the item view corresponds to the object details
        TextView nameView = (TextView) view.findViewById(R.id.nameTextView);
        TextView publisherView = (TextView) view.findViewById(R.id.publisherTextView);
        TextView genreView = (TextView) view.findViewById(R.id.gameGenreTextView);
        TextView priceView = (TextView) view.findViewById(R.id.gamePriceTextView);
        TextView availView = (TextView) view.findViewById(R.id.availTextView);

        final ImageView imageTypeView = (ImageView) view.findViewById(R.id.genreImageView);

        nameView.setText(m_cGameList.get(i).getM_sName());
        publisherView.setText(m_cGameList.get(i).getM_sPublisher());
        genreView.setText(m_cGameList.get(i).getM_sGenre());
        priceView.setText("$ " + m_cGameList.get(i).getM_sPrice());
        availView.setText(m_cGameList.get(i).getM_sAvailability());

        String avail = m_cGameList.get(i).getM_sAvailability();
        if(avail.equalsIgnoreCase("available")){
            availView.setTextColor(Color.parseColor("#299104"));
        } else {
            availView.setTextColor(Color.parseColor("#b70505"));
        }

//        String genre = m_cGameList.get(i).getM_sGenre();

//        if(genre.equalsIgnoreCase("action")){
//            imageTypeView.setImageResource(R.mipmap.action);
//        } else if(genre.equalsIgnoreCase("adventure")){
//            imageTypeView.setImageResource(R.mipmap.adventure);
//        } else if(genre.equalsIgnoreCase("arcade")){
//            imageTypeView.setImageResource(R.mipmap.arcade);
//        } else if(genre.equalsIgnoreCase("mmo")){
//            imageTypeView.setImageResource(R.mipmap.mmo);
//        } else if(genre.equalsIgnoreCase("puzzle")){
//            imageTypeView.setImageResource(R.mipmap.puzzle);
//        } else if(genre.equalsIgnoreCase("rpg")){
//            imageTypeView.setImageResource(R.mipmap.rpg);
//        } else if(genre.equalsIgnoreCase("sci-fi")){
//            imageTypeView.setImageResource(R.mipmap.scifi);
//        } else if(genre.equalsIgnoreCase("shooter")){
//            imageTypeView.setImageResource(R.mipmap.shooter);
//        } else if(genre.equalsIgnoreCase("sport")){
//            imageTypeView.setImageResource(R.mipmap.sport);
//        } else if(genre.equalsIgnoreCase("strategy")){
//            imageTypeView.setImageResource(R.mipmap.strategy);
//        }

        /*//Retrieve image the old way
        *
        * */
//        mStorageReference = FirebaseStorage.getInstance().getReference("Games/Icons/" + m_cGameList.get(i).get_id() + ".png");
//        mStorageReference.getBytes(2048 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap newMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                imageTypeView.setImageBitmap(newMap);
//            }
//        });

        //Retrieve pictures using GLIDE, GLIDE will download and cache the images
        mStorageReference = FirebaseStorage.getInstance().getReference("Games/Icons/" + m_cGameList.get(i).get_id() + ".png");
        mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();

                Glide.with(m_cCurrentContext)
                        .load(url)
                        .into(imageTypeView)
                ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        return view;
    }
}
