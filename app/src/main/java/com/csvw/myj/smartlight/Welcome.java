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

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Welcome extends AppCompatActivity implements View.OnTouchListener {
    private final String TAG = "Welcome";
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
                tcpClient = new TcpClient("192.168.43.1",8080);
                exec.execute(tcpClient);
                Message message  = Message.obtain();
                message.what = 2;
                message.obj = "lalala";
                myHandler.sendMessage(message);

                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        tcpClient.send("我是客户端");
                    }
                });

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
//                exec.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        tcpClient.send("我是服务器");
//                    }
//                });
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
                        Log.i(TAG,"收到："+ msg.obj.toString());
                        break;
                    case 2:
                        Log.i(TAG,"发送："+ msg.obj.toString());
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
                    String msg = intent.getStringExtra("tcpClientReceiver");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = msg;
                    myHandler.sendMessage(message);
                    break;
            }
        }
    }
    private void bindReceiver(){
        IntentFilter intentFilter = new IntentFilter("tcpClientReceiver");
        registerReceiver(myBroadcastReceiver,intentFilter);
    }
}
