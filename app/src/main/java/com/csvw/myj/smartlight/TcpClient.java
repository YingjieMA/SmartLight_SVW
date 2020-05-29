package com.csvw.myj.smartlight;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.csvw.myj.smartlight.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    byte content[] = new byte[160];
    private String rcvMsg;
    private int rcvLen;
    private String rcvMsgg;
    private DataOutputStream dop;

    private static TcpClient instance;
    private TcpClient(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    /**
     * 单例工厂
     * @return
     */
    public static synchronized TcpClient getInstance(){
        if(instance==null){
            instance = new TcpClient(Constants.SERVER_IP,Constants.SERVER_PORT);
        }
        return instance;
    }


//    public TcpClient(String serverIP, int serverPort) {
//        this.serverIP = serverIP;
//        this.serverPort = serverPort;
//    }
    public void closeSelf(){
        isRun = false;
    }

    /**
     * 发送String类型
     * @param msg
     */
    public void send(String msg){
        pw.println(msg);
        pw.flush();
    }

    /**
     * 发送byte[]类型
     * @param content
     */
    public void sendByte(byte[] content){
        try {
            dop.write(content,0,content.length);
            dop.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            socket = new Socket(serverIP, serverPort);
            socket.setSoTimeout(5000);
            dop = new DataOutputStream(socket.getOutputStream());
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
                    intent.setAction ("tcpClientPermission");
                    //接收到的消息拆分放入bundle
                    Bundle bundle = new Bundle();
                    bundle.putByte("permission",buff[160]);
                    bundle.putByteArray("attrs",buff);
//                    intent.putExtra("tcpClientReceiver",buff);
//                    intent.putExtra("tcpClientPermission",buff[160]);
                    intent.putExtra("tcpClientPermission",bundle);
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
            dop.close();
            is.close();
            dis.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
