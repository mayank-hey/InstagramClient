package com.mchacko.instagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mchacko on 1/25/15.
 */

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    // Takes a data item at a position and converts it to a row in the list view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Take the data source at position (i.e 0)
        // Get the data item
        InstagramPhoto photo = getItem(position);
        // Check if we are using recycled view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Lookup the subview within the template
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
        // Populate the subviews (textfields, imageview) with the correct data
        //tvCaption.setText(photo.caption);
        tvCaption.setText(Html.fromHtml("<b>" + photo.username + "</b> &nbsp -- &nbsp " + photo.caption));
        // Set the image height before loading
        imgPhoto.getLayoutParams().height = photo.imageHeight;
        // Reset the image from the recycled view
        imgPhoto.setImageResource(0);
        // Ask for the photo to be added to the imageview based on the photo url
        // Background: Send a network request to the url, download the image bytes, convert into bitmap, resizing the image, insert
        // the bitmap into the imageview
        Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
        //Picasso.with(getContext()).load(photo.profilePicUrl).transform(new CircleTransform()).into(imgProfilePic);

        //Return the view for the data item
        return convertView;
    }


    //Default, takes the model
}
