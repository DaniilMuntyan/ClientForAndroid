package com.example.androidclient;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    private int PORT;
    private int ID;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private EditText editText;
    private TextView textView;
    private FloatingActionButton fab;
    private MainActivity mainActivity;
    private Socket socket;
    private EditText editTextID;
    private EditText txtPort;
    private ImageView imageView;
    private ImageView imageView2;

    private void changeText(final String s) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(s + "\n");
            }
        });
    }
    private void changeImage(final Bitmap bit){
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bit);
            }
        });
    }
    private void init(){
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PORT = Integer.valueOf((txtPort.getText().toString()));
                ID = Integer.valueOf(editTextID.getText().toString());
            }
        });
    }
    private void click(){
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            init();
                            Message message = new Message(editText.getText().toString(), ID);
                            output.writeObject(message);
                            output.flush();
                            editText.setText("");
                            changeText("You've sent: " + message.getData().toString() + " (ID = " + ID + ")");
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void run() {
        // "0.tcp.ngrok.io"
        //"192.168.1.101"
        try {
            init();
            PORT = Integer.valueOf(txtPort.getText().toString());
            changeText("Please wait... Port: " + " " + PORT);
            socket = new Socket("0.tcp.ngrok.io", PORT);
            changeText("You have connected. Port: " + PORT);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            Bitmap bitmap = Bitmap.createBitmap(100, 100,
                    Bitmap.Config.ARGB_8888);
            bitmap = ((BitmapDrawable) imageView2.getDrawable()).getBitmap();
            BitmapDataObject bitData = new BitmapDataObject(bitmap, textView, mainActivity);
            bitData.writeObject(output);
            changeText("Sent the BitmapDataObject");
            bitData.readObject(input);
            changeText("Recieved the BitmapDataObject");
            changeImage(bitData.getImage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public Client(){

    }
    public Client(TextView textView, FloatingActionButton fab, EditText editText, EditText txtPort, ImageView imageView, EditText editTextID, ImageView imageView2, MainActivity mainActivity) {
        this.textView = textView;
        this.fab = fab;
        this.editText = editText;
        this.mainActivity = mainActivity;
        this.txtPort = txtPort;
        this.imageView = imageView;
        this.imageView2 = imageView2;
        this.editTextID = editTextID;
        ID = Integer.valueOf(editTextID.getText().toString());
    }
}
