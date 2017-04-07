package com.example.ljd.myimageprocessdemo.Thread;

import android.graphics.Bitmap;

import com.example.ljd.myimageprocessdemo.ImageSave.SaveImage;

/**
 * Created by ljd-pc on 2017/4/7.
 */
public class SaveImageThread implements Runnable {

    private Bitmap bitmap;
    private String path;
    public SaveImageThread(Bitmap bitmap,String path){
        this.bitmap = bitmap;
        this.path = path;
    }
    @Override
    public void run() {
        SaveImage.SaveImageToSDCard(bitmap,path);
    }
}
