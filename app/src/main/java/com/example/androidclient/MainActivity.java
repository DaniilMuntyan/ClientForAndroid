package com.example.androidclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private final int PORT = 16311;
    private Scanner scanner;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private EditText editText;
    private TextView textView;
    private Button button;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setEnabled(false);
        editText = (EditText)findViewById(R.id.editText);
        textView = (TextView)findViewById(R.id.textView);
        textView.setText("");
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = "0.tcp.ngrok.io"; // "0.tcp.ngrok.io"
               try{
                  // InetAddress ipAddress = InetAddress.getByName(address);
                    Socket socket = new Socket("0.tcp.ngrok.io", PORT);
                    textView.append("You have connected. Port: " + PORT + "\n");
                      System.out.println("You have connected. Port: " + PORT);
                    input = new ObjectInputStream(socket.getInputStream());
                    output = new ObjectOutputStream(socket.getOutputStream());
                    fab.setEnabled(true);
                    new Thread(new Resender(output, editText, textView, fab)).start();
                    while(true){
                        Object messageToGet = input.readObject();
                        textView.append(messageToGet.toString() + "\n");
                    }
                }catch(Exception e){
                 //   Log.e("", e.getMessage() + "");
                  //  e.printStackTrace()

                    Snackbar.make(v, e.getStackTrace() + "\n", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
