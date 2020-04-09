package com.csvw.myj.smartlight;

import android.content.Intent;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient implements Runnable {

    final private String TAG = "TcpClient";
    private String serverIP = "192.0.0.1";
    private int serverPort = 1234;
    private Socket socket = null;
    private PrintWriter pw;
    private InputStream is;
    private DataInputStream dis;
    private Boolean isRun = true;
    byte buff[] = new byte[4096];
    private String rcvMsg;
    private int rcvLen;

    public TcpClient(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }
    public void closeSelf(){
        isRun = false;
    }
    public void send(String msg){
        pw.println(msg);
        pw.flush();
    }
    @Override
    public void run() {
        try {
            socket = new Socket(serverIP, serverPort);
//            if (!socket.isConnected()){
//                isRun = false;
//            }
            socket.setSoTimeout(5000);
            pw = new PrintWriter(socket.getOutputStream(),true);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRun){
            try {
                rcvLen = dis.read(buff);
                rcvMsg = new String(buff,0,rcvLen,"utf-8");
                Log.i(TAG,"run收到消息: "+rcvMsg);
                Intent intent = new Intent();
                intent.setAction("tcpClientReceiver");
                intent.putExtra("tcpClientReceiver",rcvMsg);
                Welcome.context.sendBroadcast(intent);
                if (rcvMsg.equals("QuitClient")){
                    isRun = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            pw.close();
            is.close();
            dis.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
