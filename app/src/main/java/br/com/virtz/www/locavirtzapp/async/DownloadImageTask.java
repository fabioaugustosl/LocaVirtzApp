package br.com.virtz.www.locavirtzapp.async;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import br.com.virtz.www.locavirtzapp.util.RecuperadorImagem;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        return new RecuperadorImagem(urldisplay).recuperar();
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }


}
