package com.csvw.myj.smartlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csvw.myj.smartlight.utils.Constants;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Welcome extends AppCompatActivity implements View.OnTouchListener {
    private final String TAG = "Welcome";
    //存放接收到的数据
    byte[][] attrs = new byte[20][8];
    Button button;
    CustomDialog customDialog;
    TextView connectText;
    ImageView imageViewPositive;
    ImageView imageViewNegative;
    RelativeLayout relativeLayout;
    private static TcpClient tcpClient = null;
    Executor exec = Executors.newCachedThreadPool();
    private final MyHandler myHandler = new MyHandler(this);
    public static Context context;
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    private Timer timer;
    private MyTimerTask task;
    private int permission=2;


    public static TcpClient getTcpClient() {
        return tcpClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        bindReceiver();
//        reaadingLampView = new ReaadingLampView(Welcome.this);
//        relativeLayout = findViewById(R.id.relative);
        connectText = findViewById(R.id.content_text);
        button = findViewById(R.id.start_btn);
        button.setEnabled(false);
        CustomDialog.Builder builder = new CustomDialog.Builder(Welcome.this);
//        imageViewPositive = findViewById(R.id.positiveTextView);
//        imageViewNegative = findViewById(R.id.negativeTextView);
        builder.setMessage("请使用蓝牙或WiFi进行连接");
//        builder.setTitle("提示");
        builder.setPositiveButton(getDrawable(R.drawable.wifi_btn), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageViewPositive = customDialog.findViewById(R.id.positiveTextView);
                //设置你的操作事项
                try {
                    try{
                        checkTimeOut();
                        tcpClient = TcpClient.getInstance();
//                        tcpClient = new TcpClient(Constants.SERVER_IP,Constants.SERVER_PORT);
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                tcpClient.run();
//                            }
//                        };
                        exec.execute(tcpClient);
                        Message message  = Message.obtain();
                        message.what = 3;
                        message.obj = "客户端已连接";
                        myHandler.sendMessage(message);
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                tcpClient.send("comm check");
//                                try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }.run();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                tcpClient.send("comm check");
//                            }
//                        },0,1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    myHandler.sendEmptyMessage(5000);
                }catch (Exception e){
                    Log.e("LoginThread", e.getMessage());
                }

//                exec.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        tcpClient.send("我是客户端");
//                    }
//                });

                connectText.setText("Wifi连接成功");
                button.setEnabled(true);
//                relativeLayout.addView(reaadingLampView);
            }
        });

        builder.setNegativeButton(getDrawable(R.drawable.bluetooth_btn),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        imageViewNegative = customDialog.findViewById(R.id.negativeTextView);
//                        imageViewNegative.setBackgroundColor(Color.parseColor("#ffffff"));
                        connectText.setText("蓝牙连接成功");
                        button.setEnabled(true);
                    }
                });
        connectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        tcpClient.send("Comm check");
//                    }
//                },0,1000);
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        tcpClient.sendByte(SocketHelper.attrsArray2D21D(attrs));
                    }
                });
            }
        });
        customDialog = builder.create();
        customDialog.setCancelable(false);
        customDialog.show();
        imageViewPositive = customDialog.findViewById(R.id.positiveTextView);
        imageViewNegative = customDialog.findViewById(R.id.negativeTextView);
        imageViewPositive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        imageViewPositive.setBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case MotionEvent.ACTION_UP:
                        imageViewPositive.setBackgroundColor(Color.parseColor("#00ffffff"));
                }

                return false;
            }
        });
        imageViewNegative.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        imageViewNegative.setBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case MotionEvent.ACTION_UP:
                        imageViewNegative.setBackgroundColor(Color.parseColor("#00ffffff"));
                }

                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this,Control.class);
                startActivity(intent);
                finish();
            }
        });
        button.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(v.getId() == R.id.start_btn){
                    v.setScaleX((float)0.9);
                    v.setScaleY((float)0.9);
                }
                break;

            case MotionEvent.ACTION_UP:
                if(v.getId() == R.id.start_btn){
                    v.setScaleX(1);
                    v.setScaleY(1);
                }
                break;
        }
        return false;
    }
    private class MyHandler extends Handler{
        private WeakReference<Welcome> mActivity;

        MyHandler(Welcome activity) {
            mActivity = new WeakReference<Welcome>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if(mActivity != null){
                switch (msg.what){
                    case 1:
                        Log.i(TAG,"收到："+ msg.arg1);
                        if (msg.arg1 == 1){
                            Toast.makeText(getApplicationContext(),"有控制权，操作者模式",Toast.LENGTH_SHORT).show();
                        }else if(msg.arg1 == 0){
                            Toast.makeText(getApplicationContext(),"无控制权，观看者模式",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        Log.i(TAG,"收到："+ msg.obj.toString());
                        break;
                    case 3:
                        Log.i(TAG,"发送："+ msg.obj.toString());
                        break;
                    case 5000:
                        timer.cancel();
                        //tcpClient.closeSelf();
                        break;
                }
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            switch (mAction){
                case "tcpClientReceiver":
//                    String msg = intent.getStringExtra("tcpClientReceiver");

                    byte[] bytes = intent.getByteArrayExtra("tcpClientReceiver");
                    SocketHelper.attrsArray(attrs,bytes);
                    Log.i(TAG,""+ SocketHelper.byte2int(attrs[0][0])+","+SocketHelper.byte2int(attrs[0][1])+","+SocketHelper.byte2int(attrs[0][2])+","+SocketHelper.byte2int(attrs[0][3])+","+SocketHelper.byte2int(attrs[0][4])+","+SocketHelper.byte2int(attrs[0][5]));
//                    Log.i(TAG,""+ SocketHelper.byte2int(bytes[160]));
//                    Log.i(TAG,msg);
                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = SocketHelper.byte2int(attrs[0][0])+","+SocketHelper.byte2int(attrs[0][1])+","+SocketHelper.byte2int(attrs[0][2])+","+SocketHelper.byte2int(attrs[0][3])+","+SocketHelper.byte2int(attrs[0][4])+","+SocketHelper.byte2int(attrs[0][5]);
                    myHandler.sendMessage(message);
                    break;
                case "tcpClientPermission":
                    Bundle bundle =intent.getBundleExtra("tcpClientPermission");
                    byte b = bundle.getByte("permission");
                    byte[] attrsRcv = bundle.getByteArray("attrs");
                    SocketHelper.attrsArray(attrs,attrsRcv);
                    Log.i(TAG,""+ SocketHelper.byte2int(attrs[0][0])+","+SocketHelper.byte2int(attrs[0][1])+","+SocketHelper.byte2int(attrs[0][2])+","+SocketHelper.byte2int(attrs[0][3])+","+SocketHelper.byte2int(attrs[0][4])+","+SocketHelper.byte2int(attrs[0][5]));
//                    byte b = intent.getByteExtra("tcpClientPermission", (byte) 0x00);
                    Log.i(TAG,""+ SocketHelper.byte2int(b));

                    if (SocketHelper.byte2int(b) != permission){
                        Message msgPermission = Message.obtain();
                        msgPermission.what = 1;
                        msgPermission.arg1 = SocketHelper.byte2int(b);
                        myHandler.sendMessage(msgPermission);
                    }
                    permission =  SocketHelper.byte2int(b);
                    break;
            }
        }
    }
    private void bindReceiver(){
        IntentFilter intentFilter = new IntentFilter("tcpClientReceiver");
        intentFilter.addAction("tcpClientPermission");
        registerReceiver(myBroadcastReceiver,intentFilter);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            myHandler.sendEmptyMessage(5000);
        }
    }

    private void checkTimeOut(){
        try{
            timer = new Timer();
            task = new MyTimerTask();
            timer.schedule(task, 5000);
        }catch(Exception e){
            Log.e("timer", e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tcpClient.closeSelf();
    }
}
