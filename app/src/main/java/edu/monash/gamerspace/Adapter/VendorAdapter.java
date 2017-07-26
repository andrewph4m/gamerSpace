/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: VendorAdapter.java
Description: Contains the adapter for the vendors' list view items
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017
*/

//Implements all relevant libraries/packages
package edu.monash.gamerspace.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


import edu.monash.gamerspace.Model.Vendor;
import edu.monash.gamerspace.R;


//Vendor Adapter
public class VendorAdapter extends BaseAdapter {
    private Context m_cCurrentContext;
    private ArrayList<Vendor> m_cVendorList;

    private StorageReference mStorageReference;

    //Adapter constructor
    public VendorAdapter(Context con, ArrayList<Vendor> vendors) {
        m_cCurrentContext = con;
        m_cVendorList = vendors;
    }

    @Override
    public int getCount() {
        return m_cVendorList.size();
    }
    @Override
    public Object getItem(int i) {
        return m_cVendorList.get(i);
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

            // Create a list item based on layout list
            view = inflater.inflate(R.layout.list_vendor_item, null);
        }

        //Get the element in the design lay out and set them in the item view corresponds to the object details
        TextView nameView = (TextView) view.findViewById(R.id.vendorNameTextView);
        TextView addressView = (TextView) view.findViewById(R.id.vendorAddrTextView);

        nameView.setText(m_cVendorList.get(i).getM_sName());
        addressView.setText(m_cVendorList.get(i).getM_sAddress());

        return view;
    }
}
