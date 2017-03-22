package com.example.ljd.myimageprocessdemo.ImageGetter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ljd-pc on 2017/3/22.
 */
public class ImageNetLoader extends ImageLoader {
    private String ImageURL = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
    HttpsURLConnection httpsURLConnection = null;
    BufferedInputStream in = null;
    private static final String TAG = "ImageNetLoader";
    @Override
    public Bitmap getImage() {
        Log.v(TAG,"线程："+Thread.currentThread().getId());
        Bitmap bitmap = null;
        try {
            URL url = new URL(ImageURL);
            httpsURLConnection = (HttpsURLConnection)url.openConnection();
            in = new BufferedInputStream(httpsURLConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
