package com.example.androidclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class BitmapDataObject implements Serializable {

    private Bitmap currentImage;

    private TextView textView;
    private MainActivity main;
    private void changeText(final String s) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(s + "\n");
            }
        });
    }
    public BitmapDataObject(Bitmap bitmap){
        currentImage = bitmap;
    }
    public BitmapDataObject(Bitmap bitmap, TextView textView, MainActivity main){
        this(bitmap);
        this.textView = textView;
        this.main = main;
    }
    public void eraseColor(int c){
        currentImage.eraseColor(c);
    }

    public Bitmap getImage(){
        return currentImage;
    }
    public void writeObject(java.io.ObjectOutputStream out) throws IOException {
        changeText("In writeObject()");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String s = "";
        for(int i = 0; i < byteArray.length; i++)
            s += (i+ ") ") + byteArray[i] + ";  ";
        changeText(s);
        out.writeObject(byteArray.length);
        for(int i = 0; i < byteArray.length; ++i)
            out.writeObject(byteArray[i]);

        currentImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        changeText("In readObject()");
        int bufferLength = (Integer)in.readObject();
        changeText("After reading bufferLength");
        byte[] byteArray = new byte[bufferLength];
     /*   int pos = 0;
        do {
            int read = in.read(byteArray, pos, bufferLength - pos);

            if (read != -1) {
                pos += read;
            } else {
                break;
            }

        } while (pos < bufferLength);*/
        for(int i = 0; i < bufferLength; ++i)
            byteArray[i] = (Byte)in.readObject();
        currentImage = BitmapFactory.decodeByteArray(byteArray, 0, bufferLength);

    }
}