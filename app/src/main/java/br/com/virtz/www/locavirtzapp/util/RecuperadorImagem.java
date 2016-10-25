package br.com.virtz.www.locavirtzapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;



public class RecuperadorImagem {
    private String urldisplay = null;

    public RecuperadorImagem(String urldisplay) {
        this.urldisplay = urldisplay;
    }

    public Bitmap recuperar() {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
}