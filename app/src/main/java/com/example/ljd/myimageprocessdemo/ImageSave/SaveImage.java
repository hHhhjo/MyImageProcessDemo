package com.example.ljd.myimageprocessdemo.ImageSave;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ljd-pc on 2017/4/7.
 */
public class SaveImage {

    public static void SaveImageToSDCard(Bitmap bitmap,String path){

        File file = new File(path);
        FileOutputStream fos = null;

        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
