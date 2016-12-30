package com.example.android.ducklingplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mikasa on 13/12/16.
 */

public class SongAdapter extends ArrayAdapter<SongInfo> {

    public SongAdapter(Activity context, ArrayList<SongInfo> playlist) {
        super(context, 0, playlist);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        SongInfo currsong = getItem(position);
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.songname);
        nameTextView.setText(currsong.getsongname());
        //ImageView albumimg=(ImageView)listItemView.findViewById(R.id.albumartinfo);
        //albumimg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.treble1));

        /*MediaMetadataRetriever mmr=new MediaMetadataRetriever();
        mmr.setDataSource(currsong.getsongpath());
        byte[] art=mmr.getEmbeddedPicture();
        if(art==null)
            albumimg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.treble1));
        else
        {
            InputStream is=new ByteArrayInputStream(mmr.getEmbeddedPicture());
            Bitmap bm= BitmapFactory.decodeByteArray(art,0,art.length);
            //Drawable d=new BitmapDrawable(getContext().getResources(),currsong.bm);
            albumimg.setImageBitmap(bm);
        }*/
        int txtcolor=getContext().getResources().getColor(R.color.playlistTextColor);
        nameTextView.setTextColor(txtcolor);
        return listItemView;
    }
}

