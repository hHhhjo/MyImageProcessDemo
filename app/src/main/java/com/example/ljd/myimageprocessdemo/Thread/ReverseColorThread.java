package com.example.ljd.myimageprocessdemo.Thread;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.example.ljd.myimageprocessdemo.ImageProcess.ImageProcesser;

/**
 * Created by ljd-pc on 2017/3/22.
 */
public class ReverseColorThread implements Runnable {

    private int msgId;
    private Handler handler = null;
    private Bitmap src;
    public ReverseColorThread(Handler handler, int msgId, Bitmap src){
        this.handler = handler;
        this.msgId = msgId;
        this.src = src;
    }
    @Override
    public void run() {
        Bitmap bitmap = ImageProcesser.RerverseColor(src);
        Message message = new Message();
        message.what = msgId;
        message.obj = bitmap;
        handler.sendMessage(message);
    }
}
