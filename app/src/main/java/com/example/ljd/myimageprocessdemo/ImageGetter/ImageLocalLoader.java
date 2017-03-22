package com.example.ljd.myimageprocessdemo.ImageGetter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by ljd-pc on 2017/3/22.
 */
public class ImageLocalLoader extends ImageLoader{
    private static String ImageLocalPath = Environment.getExternalStorageDirectory().toString() + File.separator
            + "DCIM/Camera/IMG_20170322_140432.jpg";
    private static final String TAG = "ImageLocalLoader";
    @Override
    public Bitmap getImage() {
        Log.v(TAG,"线程："+Thread.currentThread().getId());
        File mFile=new File(ImageLocalPath);
        Bitmap bitmap = null;
        if (mFile.exists()) {
            bitmap= BitmapFactory.decodeFile(ImageLocalPath);
        }
        return bitmap;
    }
}
