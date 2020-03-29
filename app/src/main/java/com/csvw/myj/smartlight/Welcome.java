package com.csvw.myj.smartlight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csvw.myj.smartlight.LightView.ReaadingLampView;

public class Welcome extends AppCompatActivity implements View.OnTouchListener {
    Button button;
    CustomDialog customDialog;
    TextView connectText;
    ImageView imageViewPositive;
    ImageView imageViewNegative;
    ReaadingLampView reaadingLampView;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        reaadingLampView = new ReaadingLampView(Welcome.this);
        relativeLayout = findViewById(R.id.relative);
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
                imageViewPositive.setBackgroundColor(Color.parseColor("#ffffff"));
                connectText.setText("Wifi连接成功");
                button.setEnabled(true);
                relativeLayout.addView(reaadingLampView);
            }
        });

        builder.setNegativeButton(getDrawable(R.drawable.bluetooth_btn),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        imageViewNegative = customDialog.findViewById(R.id.negativeTextView);
                        imageViewNegative.setBackgroundColor(Color.parseColor("#ffffff"));
                        connectText.setText("蓝牙连接成功");
                        button.setEnabled(true);
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
}
