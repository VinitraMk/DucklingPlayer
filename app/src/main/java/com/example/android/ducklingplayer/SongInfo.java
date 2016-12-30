package com.example.android.ducklingplayer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.security.AccessController.getContext;

/**
 * Created by mikasa on 13/12/16.
 */

public class SongInfo implements java.io.Serializable {
    String msongname,msongpath;
    Bitmap bm;
    public SongInfo(String songname,String songpath)
    {
        msongname=songname;
        msongpath=songpath;

    }

    public String getsongname(){ return msongname; }

    public String getsongpath(){ return msongpath; }




}

