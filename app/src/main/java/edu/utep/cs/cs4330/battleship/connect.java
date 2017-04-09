package edu.utep.cs.cs4330.battleship;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Handler;



public class connect {

public static Socket socket;
    //////////////////////////////////////////////////////////
    /*
    public static void connectToServer(final String host, final int port) {
        new Thread(new Runnable()  {
            @Override
            public void run() {
                socket = createSocket(host, port);
                if (socket != null) {
                    //try {
                    //readMessage();
                    //} catch (IOException e) {
                    //  e.printStackTrace();
                    //}
                    // WRITE YOUR CODE HERE ...
                    //if(msgEdit.getText().toString()!= null) {
                    //    sendMessage(msgEdit.getText().toString());
                    //}
                }


            }

            /*
            handler.post(new Runnable() {
            @Override
            public void run(){
            toast(socket != null ? "Connected." : "Failed to connect!11"));
            }
            });

        }).start();
    }

    public static Socket createSocket(String host, int port) {
        try {
            return new Socket(host, port);
        } catch (Exception e) {
            Log.d("TAG---", e.toString());
        }
        return null;
    }

    public static void sendMessage(String msg) {
        // WRITE YOUR CODE HERE ...

        PrintWriter out = null;
        try {
            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println(msg);
        out.flush();
        //displayMessage(msg);
    }
    //protected void toast(String msg) {
    //    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
   // }
    public Socket getSocket(){
        return socket;
    }
    public void setSocket(Socket s){
        //socket=s;
    }

*/
}
