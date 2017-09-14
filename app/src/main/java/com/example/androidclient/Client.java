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

    private final int PORT = 5165;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private EditText editText;
    private TextView textView;
    private FloatingActionButton fab;
    private EditText txtPort;
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
                            changeText("SEND");
                            Message message = new Message(editText.getText().toString(), -1);
                            output.writeObject(message);
                            output.flush();
                            changeText("You've sent: + " + editText.getText().toString() + "\n");
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
        String address = "localhost"; // "0.tcp.ngrok.io"
        try {
            changeText("Hm...\n");
            socket = new Socket("192.168.1.101", PORT);
            changeText("You have connected. Port: " + PORT + "\n");
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
          //  changeText("the end of Savelyev");
            click();
           // new Thread(new Resender(output, editText, textView, fab, mainActivity)).start();
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
        this.txtPort = txtPort;
    }
}
