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
    private MainActivity mainActivity;
    private String textFrom;
    private int ID;
    Resender(ObjectOutputStream output, EditText editText, TextView textView, FloatingActionButton fab, EditText editTextID, MainActivity mainActivity){
        this.editText = editText;
        this.output = output;
        this.textView = textView;
        this.fab = fab;
        this.mainActivity = mainActivity;
        textFrom = editText.getText().toString();
        ID = Integer.valueOf(editTextID.getText().toString());
    }
    private void changeText(final String s) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(s + "\n");
            }
        });
    }
  /*  private void getTextfromText(){
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textFrom = (editText).getText().toString();
            }
        });
    }*/
    Resender(){}
    @Override
    public void run(){
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        while (true) {
                            //getTextfromText();
                            Message messageToSend = new Message(textFrom, ID);
                            output.writeObject(messageToSend);
                            output.flush();
                        }
                    }catch(IOException e){
                        Snackbar.make(view, e.getStackTrace() + "\n", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        });

    }
}
