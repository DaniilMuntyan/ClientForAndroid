package com.example.androidclient;

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

    public void changeText(final String s) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(s + "\n");
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
        try {
            click();
            init();
            PORT = Integer.valueOf(txtPort.getText().toString());
            changeText("Please wait... Port: " + " " + PORT);
            socket = new Socket("0.tcp.ngrok.io", PORT);
            changeText("You have connected. Port: " + PORT);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            while(true){
                Object messageToGet = input.readObject();
                changeText(messageToGet.toString());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public Client(){

    }
    public Client(TextView textView, FloatingActionButton fab, EditText editText, EditText txtPort, ImageView imageView, EditText editTextID, MainActivity mainActivity) {
        this.textView = textView;
        this.fab = fab;
        this.editText = editText;
        this.mainActivity = mainActivity;
        this.txtPort = txtPort;
        this.editTextID = editTextID;
        ID = Integer.valueOf(editTextID.getText().toString());
    }
}
