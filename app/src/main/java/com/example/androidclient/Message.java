package com.example.androidclient;

import java.io.Serializable;

public class Message implements Serializable {

    private Object data;

    private int idReceiver;

    private int idSender;

    public Object getData() {
        return data;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public Message(Object data, int idReceiver) {
        this.data = data;
        this.idReceiver = idReceiver;
    }

    @Override
    public String toString() {
        if(idSender==-1)
            return "Server has sent: " + data;
        return "User № " + idSender + " has sent: " + data;
    }
}
