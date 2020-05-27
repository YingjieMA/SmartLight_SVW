package com.csvw.myj.smartlight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class TcpClient implements Runnable {

    final private String TAG = "TcpClient";
    private String serverIP = "192.168.10.2";
    private int serverPort = 8080;
    private Socket socket = null;
    private PrintWriter pw;
    private InputStream is;
    private DataInputStream dis;
    private Boolean isRun = true;
    byte buff[] = new byte[4096];
    private String rcvMsg;
    private int rcvLen;
    private String rcvMsgg;

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
            socket.setSoTimeout(5000);
            pw = new PrintWriter(socket.getOutputStream(),true);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRun){
            try {
//                    new SocketHelper().commCheck(pw);
                rcvLen = dis.read(buff);
                if (rcvLen!=-1){
                    rcvMsg = new String(buff,0,rcvLen,"utf-8");
                    rcvMsgg = new SocketHelper().byte2hex(buff,rcvLen);
                    Log.i(TAG,"run收到消息: "+ new SocketHelper().byte2hex(buff,rcvLen));
//                    Log.i(TAG,"run收到消息: "+ new SocketHelper().arrPrint(new SocketHelper().byte2hex(buff,rcvLen)));
                    Log.i(TAG,"run收到消息: "+ rcvMsg);
                    if (rcvMsg.equals("accept")){
                        new SocketHelper().commCheck(pw);
                        continue;
                    }
                    Intent intent = new Intent();
                    intent.setAction("tcpClientReceiver");
                    intent.setAction("tcpClientPermission");
                    //接收到的消息拆分放入bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("id","s");
                    intent.putExtra("tcpClientReceiver",buff);
                    intent.putExtra("tcpClientPermission",buff[160]);
//                    intent.putExtra("tcpClientReceiver",rcvMsgg);
                    Welcome.context.sendBroadcast(intent);
//                    new SocketHelper().commCheck(pw);
                    if (rcvMsg.equals("QuitClient")){
                        isRun = false;
                    }
                }

            }catch (NullPointerException e){
                Log.i(TAG,"连接不到服务器");
                try {
                    socket = new Socket(serverIP, serverPort);
                    socket.setSoTimeout(5000);
                    pw = new PrintWriter(socket.getOutputStream(),true);
                    is = socket.getInputStream();
                    dis = new DataInputStream(is);
                } catch (IOException m) {
                    m.printStackTrace();
                }
//                closeSelf();
            }catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG,"连接异常");
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
