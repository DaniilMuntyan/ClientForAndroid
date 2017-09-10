package com.example.androidclient;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Resender extends Thread{
    private Scanner scanner;
    private ObjectOutputStream output;
    private Socket socket;
    private EditText editText;
    private TextView textView;
    private FloatingActionButton fab;
    Resender(ObjectOutputStream output, EditText editText, TextView textView, FloatingActionButton fab){
        this.editText = editText;
        this.output = output;
        this.textView = textView;
        this.fab = fab;
    }
    Resender(){}
    @Override
    public void run(){
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        while (true) {
                            String s;
                            Integer i;
                            s = editText.getText().toString();
                            Message messageToSend = new Message(s, -1);
                            output.writeObject(messageToSend);
                            output.flush();
                            textView.append("You've sent: + " + s + "\n");
                        }
                    }catch(IOException e){
                        Snackbar.make(view, e.getStackTrace() + "\n", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        });

    }
}
