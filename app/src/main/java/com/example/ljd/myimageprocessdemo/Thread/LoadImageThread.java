package com.example.ljd.myimageprocessdemo.Thread;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.example.ljd.myimageprocessdemo.ImageGetter.ImageLoader;
import com.example.ljd.myimageprocessdemo.MainActivity;

/**
 * Created by ljd-pc on 2017/3/22.
 */
public class LoadImageThread implements Runnable{
    ImageLoader imageLoader = null;
    Handler handler = null;
    private int msgId;
    public LoadImageThread(ImageLoader imageLoader, Handler handler,int msgId){
        this.imageLoader = imageLoader;
        this.handler = handler;
        this.msgId = msgId;
    }
    @Override
    public void run() {
        Bitmap bitmap = imageLoader.getImage();
        Message message = new Message();
        message.what = msgId;
        message.obj = bitmap;
        handler.sendMessage(message);
    }
}
