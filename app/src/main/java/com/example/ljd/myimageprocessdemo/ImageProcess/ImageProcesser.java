package com.example.ljd.myimageprocessdemo.ImageProcess;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.nio.IntBuffer;
/**
 * Created by ljd-pc on 2017/3/22.
 */
public class ImageProcesser {

    private static final String TAG = "ImageProcesser";
    private static int width;
    private static int height;
    private static int pixelColor;
    private static int A,R,G,B;
    private static int[] colors;
    public static Bitmap RerverseColor(Bitmap src){
        Log.v(TAG,"线程："+Thread.currentThread().getId());
        Bitmap bitmap;
        width = src.getWidth();
        height = src.getHeight();
        colors = new int[width*height];
        for(int y= 0;y<height;y++){
            for(int x=0;x<width;x++){
                pixelColor = src.getPixel(x, y);
                A = Color.alpha(pixelColor);
                R = 255-Color.red(pixelColor);
                G = 255-Color.green(pixelColor);
                B = 255-Color.blue(pixelColor);
                colors[y*width+x] = (A<<24)|((R&0x000000ff)<<16)|((G&0x000000ff)<<8)|(B&0x000000ff);
            }
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(colors));
        return bitmap;
    }
}
