package com.csvw.myj.smartlight;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class MainActivity extends Activity {
    static float density;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
// 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        Log.e("宽度=======>",screenWidth+"");
        Log.e("高度=======>",screenHeight+"");
        Log.e("宽度==像素=====>",width+"");
        Log.e("高度==像素=====>",height+"");
        Log.e("高度==比率=====>",density+"");

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //你需要跳转的地方的代码
                Intent intent = new Intent(MainActivity.this, Welcome.class);
                startActivity(intent);
                finish();
            }
        }, 2000); //延迟2秒跳转

    }
}
