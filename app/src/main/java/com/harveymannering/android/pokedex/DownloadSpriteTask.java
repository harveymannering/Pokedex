package com.harveymannering.android.pokedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;

//Background task used to download pokemon sprite from a url
public class DownloadSpriteTask extends AsyncTask<String, Void, Bitmap> {
    //Destination image view for the pokemon sprite
    ImageView imageView;
    //Pokemon's position in the main ArrayList
    int position;

    public DownloadSpriteTask(ImageView imageView, int position) {
        this.imageView = imageView;
        this.position = position;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap pokemon_sprite = null;
        try {
            //Downloads the image from a given url
            InputStream in = new java.net.URL(url).openStream();
            //Saves image into a bitmap
            pokemon_sprite = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return pokemon_sprite;
    }
}
