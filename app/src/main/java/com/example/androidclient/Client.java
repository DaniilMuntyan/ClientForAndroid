package com.example.androidclient;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    private int PORT = 12145;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private EditText editText;
    private TextView textView;
    private FloatingActionButton fab;
    private MainActivity mainActivity;
    private Socket socket;

    private void changeText(final String s) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(s);
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
                            Message message = new Message(editText.getText().toString(), -1);
                            output.writeObject(message);
                            output.flush();
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
            changeText("Please wait...\n");
            socket = new Socket("0.tcp.ngrok.io", PORT);
            changeText("You have connected. Port: " + PORT + "\n");
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            click();
            while(true){
                Object messageToGet = input.readObject();
                changeText(messageToGet.toString() + "\n");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Client(TextView textView, FloatingActionButton fab, EditText editText, EditText txtPort, MainActivity mainActivity) {
        this.textView = textView;
        this.fab = fab;
        this.editText = editText;
        this.mainActivity = mainActivity;
        this.PORT = Integer.valueOf(txtPort.getText().toString());
    }
}
