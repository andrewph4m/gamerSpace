/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: NewsFeedActivity.java
Description: Contains the activity for updating the news gathered from various RSS Feed for the app
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017
*/

//Implements all relevant libraries/packages

package edu.monash.gamerspace.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


import edu.monash.gamerspace.R;
import edu.monash.gamerspace.Model.RssFeedModel;

///RSS FEED
    /*Courtesy of Rss Feed code:
    * Ogbo, O. (2016, December 9). Building a simple RSS reader – full tutorial with code | Android Authority.
    * Retrieved from Android Authority: http://www.androidauthority.com/simple-rss-reader-full-tutorial-733245/
    * */

    //Using recycler view
public class RssFeedAdapter extends RecyclerView.Adapter<RssFeedAdapter.FeedModelViewHolder>{
    private List<RssFeedModel> mRssFeedModels;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }

    public RssFeedAdapter(List<RssFeedModel> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_feed_item, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);

//        ((ImageView)holder.rssFeedView.findViewById(R.id.imagePicture))
//        String postThumbViewURL;

        ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);

        //Retrieve the list item from the layout file and modify the content as retrieved from the rss link
        //which cosists primarily of the title, description and link
        ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setClickable(true);

        //Format the xml elements from the rss feeds so that they are readable by the user (hiding all of the html gists)
        //Also display the links in the description

        ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setText(Html.fromHtml(rssFeedModel.description));


        //Modify the link so that it will be formatted and clickable, users will be able to access to the web browser and see the news
        TextView link = (TextView)holder.rssFeedView.findViewById(R.id.linkText);

        link.setClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());

        String text = "<a href='"+ rssFeedModel.link +"'> Read more... </a>";
        link.setText(Html.fromHtml(text));
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

}
