package com.csvw.myj.smartlight;


import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.csvw.myj.smartlight.sensor.AccelerometerManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Control extends Activity implements OnColorChangedListener, AccelerometerListener {
    final String TAG = "Control";
    //存放接收到的数据
    byte[] attrsRvcPre = new byte[160];
    byte[][] attrs = new byte[20][8];
    private final Control.MyHandler myHandler = new Control.MyHandler(this);
    private Control.MyBroadcastReceiver myBroadcastReceiver = new Control.MyBroadcastReceiver();
    private ImageView carImage;
    private RelativeLayout rl;
    private ListView lvLights;
    private GridView gvTamplate;
    private List<Light> lightsList;
    private ArrayList<Light> rgbLightsList;
    private List<Light> smartLightsList;
    private List<ColorTemplate> colorTemplateList;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private View view;
    private LayoutInflater inflater;
    LightsDataAdapter adapter;
    TemplatesDataAdapter templatesDataAdapter;
    MoodDataAdapter moodDataAdapter;
    ColorPickerView colorPickerView;

    Boolean[] notHandleAfterTextChangedEvent = {false, false, false, false};
    Boolean[] changeFromSeekBar = {false, false, false, false};
    Boolean[] changeFromEditText = {false, false, false, false};

    private static int r = 255, g = 255, b = 255, i = 255;

    //Thumb图片大小 P20 200
    private int seekWidth = (int) (MainActivity.density * 50 + 0.5f);
    private int seekHeight = (int) (MainActivity.density * 50 + 0.5f);
    //用于隐藏的View
    View hiddenView;

    FrameLayout frameLayout;
    LightView reaadingLampView;
    LightView doorOpenerLampView;
    LightView footwellLampView;
    LightView makeupLampView;
    LightView mikoLampUpView;
    //rgbLight
    LightView ipDecorLampView;
    LightView doorDecorLampVo;
//    RGBLightView ipDecorLampView;

    LinearLayout linearLayout;
    private ImageView imageView5;
    private int permission;
    private GetLightList getLightList;
    //线程池
    ExecutorService exec = Executors.newCachedThreadPool();
    private ArrayList<Light> freedomRgbLightList;
    //定时器需要
    long timeLast = 0;
    //振动
    private Vibrator mVibrator;
    //记录是否保存mood-freedom
    private boolean savemood = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        bindReceiver();
        //添加灯View
        frameLayout = findViewById(R.id.frameLayout);

        linearLayout = findViewById(R.id.control_layout);
        carImage = findViewById(R.id.carImage);
        this.rl = findViewById(R.id.rl);
        this.inflater = LayoutInflater.from(this);
        this.view = inflater.inflate(R.layout.activity_epel, rl, false);
        this.lvLights = view.findViewById(R.id.lvLights);
        this.rl.addView(view);
        //获取灯List
        getLightList = new GetLightList();
        lightsList = getLightList.getWhiteLampList();
        rgbLightsList = getLightList.getRgbLampList();
        smartLightsList = getLightList.getSmartLightList();
        //获取VW30色List
        colorTemplateList = new GetColorTableList().getAllList();
        this.initialize(lightsList);
        imageView1 = findViewById(R.id.white_lamp_btn);
        imageView2 = findViewById(R.id.rgb_lamp_btn);
        imageView3 = findViewById(R.id.smartlight_btn);
        imageView4 = findViewById(R.id.setting_btn);
        imageView5 = findViewById(R.id.mood_btn);
        imageView1.setOnClickListener(l);
        imageView2.setOnClickListener(l);
        imageView3.setOnClickListener(l);
        imageView4.setOnClickListener(l);
        imageView5.setOnClickListener(l);
        //carImage
        carImage = findViewById(R.id.carImage);
        carImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("坐标", "x: " + event.getX());
                Log.i("坐标", "Y: " + event.getY());
                return true;
            }
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        if (action.equals("permission")) {
            permission = intent.getIntExtra("permission", 1);
            Log.i(TAG, "Permission = " + permission);
        }
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
    }

    //单击事件监听器
    View.OnClickListener l = new View.OnClickListener() {


        private View hiddenView;
        private View listView;
        private View hiddenView2;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.white_lamp_btn:
                    imageView1.setImageResource(R.drawable.white_light_btn_2_blue);
                    imageView2.setImageResource(R.drawable.rgb_light_btn_2);
                    imageView3.setImageResource(R.drawable.smartlight_btn_2);
                    imageView4.setImageResource(R.drawable.setting_btn_2);
                    imageView5.setImageResource(R.drawable.mood_btn);
                    initialize(lightsList);
                    hiddenView = findViewById(R.id.cycle_layout);
                    hiddenView2 = findViewById(R.id.moodTableView);
                    if (null != hiddenView) {
                        ViewGroup parent = (ViewGroup) hiddenView.getParent();
                        parent.removeView(hiddenView);
                        rl.addView(view);
                        initialize(lightsList);
                    } else if (null != hiddenView2) {
                        ViewGroup parent = (ViewGroup) hiddenView2.getParent();
                        parent.removeView(hiddenView2);
                        view = inflater.inflate(R.layout.activity_epel, rl, false);
                        lvLights = view.findViewById(R.id.lvLights);
                        rl.addView(view);
                        initialize(lightsList);
                    }
                    break;
                case R.id.rgb_lamp_btn:
                    imageView1.setImageResource(R.drawable.white_light_btn_2);
                    imageView2.setImageResource(R.drawable.rgb_light_btn_2_blue);
                    imageView3.setImageResource(R.drawable.smartlight_btn_2);
                    imageView4.setImageResource(R.drawable.setting_btn_2);
                    imageView5.setImageResource(R.drawable.mood_btn);
                    listView = findViewById(R.id.linearlayout1);
                    hiddenView2 = findViewById(R.id.moodTableView);
                    if (null != listView) {
                        initialize((rgbLightsList));
                    }
                    hiddenView = findViewById(R.id.cycle_layout);
                    if (null != hiddenView) {
                        ViewGroup parent = (ViewGroup) hiddenView.getParent();
                        parent.removeView(hiddenView);
                        rl.addView(view);
                        initialize(rgbLightsList);
                    } else if (null != hiddenView2) {
                        ViewGroup parent = (ViewGroup) hiddenView2.getParent();
                        parent.removeView(hiddenView2);
                        view = inflater.inflate(R.layout.activity_epel, rl, false);
                        lvLights = view.findViewById(R.id.lvLights);
                        rl.addView(view);
                        initialize(rgbLightsList);
                    }
                    break;
                case R.id.smartlight_btn:
                    imageView1.setImageResource(R.drawable.white_light_btn_2);
                    imageView2.setImageResource(R.drawable.rgb_light_btn_2);
                    imageView3.setImageResource(R.drawable.smartlight_btn_2_blue);
                    imageView4.setImageResource(R.drawable.setting_btn_2);
                    imageView5.setImageResource(R.drawable.mood_btn);
                    Control.this.initialize(smartLightsList);
                    listView = findViewById(R.id.linearlayout1);
                    hiddenView2 = findViewById(R.id.moodTableView);
                    if (null != listView) {
                        initialize((rgbLightsList));
                    }
                    hiddenView = findViewById(R.id.cycle_layout);
                    if (null != hiddenView) {
                        ViewGroup parent = (ViewGroup) hiddenView.getParent();
                        parent.removeView(hiddenView);
                        rl.addView(view);
                        initialize(rgbLightsList);
                    } else if (null != hiddenView2) {
                        ViewGroup parent = (ViewGroup) hiddenView2.getParent();
                        parent.removeView(hiddenView2);
                        view = inflater.inflate(R.layout.activity_epel, rl, false);
                        lvLights = view.findViewById(R.id.lvLights);
                        rl.addView(view);
                        initialize(smartLightsList);
                    }
                    break;
                case R.id.setting_btn:
                    imageView1.setImageResource(R.drawable.white_light_btn_2);
                    imageView2.setImageResource(R.drawable.rgb_light_btn_2);
                    imageView3.setImageResource(R.drawable.smartlight_btn_2);
                    imageView4.setImageResource(R.drawable.setting_btn_2_blue);
                    imageView5.setImageResource(R.drawable.mood_btn);
                    break;
                case R.id.mood_btn:
                    imageView1.setImageResource(R.drawable.white_light_btn_2);
                    imageView2.setImageResource(R.drawable.rgb_light_btn_2);
                    imageView3.setImageResource(R.drawable.smartlight_btn_2);
                    imageView4.setImageResource(R.drawable.setting_btn_2);
                    imageView5.setImageResource(R.drawable.mood_btn_blue);
                    hiddenView = findViewById(R.id.cycle_layout);
                    hiddenView2 = findViewById(R.id.linearlayout1);
                    if (null != hiddenView) {
                        ViewGroup parent = (ViewGroup) hiddenView.getParent();
                        parent.removeView(hiddenView);
                        view = inflater.inflate(R.layout.mood_table, rl, false);
                        rl.addView(view);
                        initializeMood(new GetMoodTemplateList().getAllList(), view);
                    } else if (null != hiddenView2) {
                        rl.removeView(hiddenView2);
                        view = inflater.inflate(R.layout.mood_table, rl, false);
                        rl.addView(view);
                        initializeMood(new GetMoodTemplateList().getAllList(), view);
                    }
                default:
                    break;
            }
        }
    };

    /**
     * 初始化成员lightsList
     */
    private void initialize(List<Light> lightsList) {
        adapter = new LightsDataAdapter(this, lightsList);
        this.lvLights.setAdapter(adapter);

    }


    /**
     * 初始化Mood
     *
     * @param moodTemplateList
     */
    private void initializeMood(List<MoodTemplate> moodTemplateList, View view) {
        moodDataAdapter = new MoodDataAdapter(this, moodTemplateList, this);
        gvTamplate = view.findViewById(R.id.template_gridview_mood);
        this.gvTamplate.setAdapter(moodDataAdapter);

    }

    /**
     * 初始化30色标准
     *
     * @param colorTemplateList
     */
    private void initializeTamplates(List<ColorTemplate> colorTemplateList) {
        templatesDataAdapter = new TemplatesDataAdapter(this, colorTemplateList, this);
        this.gvTamplate.setAdapter(templatesDataAdapter);

    }

    /***
     * 添加LightView
     */
    public void addLightView(String string) {
        switch (string) {
            case "Reading Lamp":
                if (null != reaadingLampView) {
                } else {
                    reaadingLampView = new LightView(Control.this, R.drawable.reading_lamp, 0, 0);
                    frameLayout.addView(reaadingLampView);
                }
                break;
            case "Door Opener Lamp":
                if (null != doorOpenerLampView) {
                } else {
                    doorOpenerLampView = new LightView(Control.this, R.drawable.door_opener_lamp, 0, 221.5f);
                    frameLayout.addView(doorOpenerLampView);
                }
                break;
            case "Footwell Lamp_vo.":
                if (null != footwellLampView) {
                } else {
                    footwellLampView = new LightView(Control.this, R.drawable.footwell_lamp, 0, 305.13f);
                    frameLayout.addView(footwellLampView);
                }
                break;
            case "Makeup Lamp":
                if (null != makeupLampView) {
                } else {
                    makeupLampView = new LightView(Control.this, R.drawable.makeup_lamp, 0, 0);
                    frameLayout.addView(makeupLampView);
                }
                break;
            case "MIKO Lamp_up":
                if (null != mikoLampUpView) {
                } else {
                    mikoLampUpView = new LightView(Control.this, R.drawable.miko_lamp_up, 0, 346.1f);
                    frameLayout.addView(mikoLampUpView);
                }
                break;
            default:
                break;
        }
    }

    /***
     * 移除LightView
     */
    public void removeLightView(String string) {
        switch (string) {
            case "Reading Lamp":
                if (null != reaadingLampView) {
                    ViewGroup parent = (ViewGroup) reaadingLampView.getParent();
                    parent.removeView(reaadingLampView);
                    reaadingLampView = null;
                }
                break;
            case "Door Opener Lamp":
                if (null != doorOpenerLampView) {
                    ViewGroup parent = (ViewGroup) doorOpenerLampView.getParent();
                    parent.removeView(doorOpenerLampView);
                    doorOpenerLampView = null;
                }
                break;
            case "Footwell Lamp_vo.":
                if (null != footwellLampView) {
                    ViewGroup parent = (ViewGroup) footwellLampView.getParent();
                    parent.removeView(footwellLampView);
                    footwellLampView = null;
                }
                break;
            case "Makeup Lamp":
                if (null != makeupLampView) {
                    ViewGroup parent = (ViewGroup) makeupLampView.getParent();
                    parent.removeView(makeupLampView);
                    makeupLampView = null;
                }
                break;
            case "MIKO Lamp_up":
                if (null != mikoLampUpView) {
                    ViewGroup parent = (ViewGroup) mikoLampUpView.getParent();
                    parent.removeView(mikoLampUpView);
                    mikoLampUpView = null;
                }
                break;
            default:
                break;
        }
    }

    /***
     * 添加RGBLightView
     */
    public void addRGBLightView(String string, int color) {
        switch (string) {
            case "IP Decor Lamp":
                if (null != ipDecorLampView) {
                } else {
                    ipDecorLampView = new LightView(Control.this, R.drawable.ip_decor_lamp, 0, 255.69f, color);
                    frameLayout.addView(ipDecorLampView);
                }
                break;
            case "Starry Lamp":
                break;
            case "Door Decor Lamp_vo.":
                if (null != doorDecorLampVo) {
                } else {
                    doorDecorLampVo = new LightView(Control.this, R.drawable.door_decor_lamp_vo, 0, 252.43f, color);
                    frameLayout.addView(doorDecorLampVo);
                }
                break;
            default:
                break;
        }
    }

    /***
     * 移除RGBLightView
     */
    public void removeRGBLightView(String string) {
        switch (string) {
            case "IP Decor Lamp":
                if (null != ipDecorLampView) {
                    ViewGroup parent = (ViewGroup) ipDecorLampView.getParent();
                    parent.removeView(ipDecorLampView);
                    ipDecorLampView = null;
                }
                break;
            case "Door Decor Lamp_vo.":
                if (null != doorDecorLampVo) {
                    ViewGroup parent = (ViewGroup) doorDecorLampVo.getParent();
                    parent.removeView(doorDecorLampVo);
                    doorDecorLampVo = null;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void colorChanged(int color) {
        RgbLight light = (RgbLight) adapter.light;
        if (light == null)
            return;
        TextView tv = findViewById(R.id.light_name);
        if (null != tv) {
            tv.setTextColor(color);
            light.setrValue((color & 0xff0000) >> 16);
            light.setgValue((color & 0x00ff00) >> 8);
            light.setbValue((color & 0x0000ff));
            String lightName = (String) tv.getText();
            switch (lightName) {
                case "IP Decor Lamp":
                    if (ipDecorLampView != null) {
                        ViewGroup parent = (ViewGroup) ipDecorLampView.getParent();
                        parent.removeView(ipDecorLampView);
                        ipDecorLampView = new LightView(Control.this, R.drawable.ip_decor_lamp, 0, 255.69f, color);
                        frameLayout.addView(ipDecorLampView);
                    }
                    break;
                case "Door Decor Lamp_vo.":
                    if (doorDecorLampVo != null) {
                        ViewGroup parent = (ViewGroup) doorDecorLampVo.getParent();
                        parent.removeView(doorDecorLampVo);
                        doorDecorLampVo = new LightView(Control.this, R.drawable.door_decor_lamp_vo, 0, 252.43f, color);
                        frameLayout.addView(doorDecorLampVo);
                        doorOpenerLampView = null;
                    }
                    break;
                default:
                    break;
            }
        }
        r = light.getrValue();
        g = light.getgValue();
        b = light.getbValue();
        i = light.getIntenseValue();
        Log.i("11", adapter.light.toString());
        if (TcpClient.getInstance() != null) {
//            sendBtyesData2HW();
//            sendByteData2Controller(r,g,b,i);
            sendByteData2Controller();
        }
    }

    /**
     * OnTouch 效果
     */
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setScaleX((float) 0.97);
                    v.setScaleY((float) 0.97);
                    break;

                case MotionEvent.ACTION_UP:
                    v.setScaleX(1);
                    v.setScaleY(1);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setScaleX(1);
                    v.setScaleY(1);
            }
            return false;
        }
    };

    @Override
    public void onAccelerationChanged(float x, float y, float z) {

    }

    /**
     * 摇一摇事件
     *
     * @param force
     */
    @Override
    public void onShake(float force) {
        //VW30色随机一种颜色
//        int max = 29, min = 0;
//        int num = (int) (Math.random() * (max - min) + min);
//        String color = colorTemplateList.get(num).getLightColor();
//        Log.i(TAG, "num:" + num);
//        colorChanged(Color.parseColor(color));
////        int max = 255, min =0;
////        r = (int) (Math.random()*(max-min)+min);
////        g= (int) (Math.random()*(max-min)+min);
////        b = (int) (Math.random()*(max-min)+min);
////        colorChanged(Color.rgb(r,g,b));
//        if (TcpClient.getInstance() != null) {
////            sendBtyesData2HW();
////            sendByteData2Controller(r,g,b,i);
//            sendByteData2Controller();
//        }

        if (System.currentTimeMillis() > (timeLast + 800)) {//0.8秒 内不在处理
            timeLast = System.currentTimeMillis();
            int max = 29, min = 0;
            int num = (int) (Math.random() * (max - min) + min);
            String color = colorTemplateList.get(num).getLightColor();
            Log.i(TAG, "num:" + num);
            colorChanged(Color.parseColor(color));
            mVibrator.vibrate(200);// 设置震动
            if (TcpClient.getInstance() != null) {
                sendByteData2Controller();
            }
        }
    }


    /**
     * ListView数据适配器
     */
    private class LightsDataAdapter extends BaseAdapter {
        //定义操作的灯
        public Light light;
        public List<Light> lightsList;
        //用于把布局文件转化为View对象
        private LayoutInflater layoutInflater;

        public LightsDataAdapter(Context context, List<Light> lightsList) {
            this.lightsList = lightsList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return lightsList.size();
        }

        @Override
        public Object getItem(int position) {
            return lightsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public class ViewHolder {
            RelativeLayout relativeLayout;
            TextView tvName;
            Switch swBtn;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            View viewItem = null;
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewItem = layoutInflater.inflate(R.layout.listview_item_light, null);
                viewHolder = new ViewHolder();
                viewHolder.relativeLayout = viewItem.findViewById(R.id.list_item);
                viewHolder.tvName = viewItem
                        .findViewById(R.id.tvName);
                viewHolder.swBtn = viewItem.findViewById(R.id.btn_sw);
                viewItem.setTag(viewHolder);
            } else {
                viewItem = convertView;
                viewHolder = (ViewHolder) viewItem.getTag();
            }

            final Light le = lightsList.get(position);
            if (le.getType().equals("white") || le.getType().equals("smart")) {
                if (le.getState()) {
                    addLightView(le.getName());
                    viewHolder.relativeLayout.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    viewHolder.tvName.setTextColor(Color.parseColor("#000000"));
                } else {
                    removeLightView(le.getName());
                    viewHolder.relativeLayout.setBackgroundColor(getColor(R.color.lightOff));
                    viewHolder.tvName.setTextColor(Color.parseColor("#ffffff"));
                }
            } else {
                if (le.getState()) {
                    addRGBLightView(le.getName(), Color.argb(255, ((RgbLight) le).getrValue(), ((RgbLight) le).getgValue(), ((RgbLight) le).getbValue()));
                    viewHolder.relativeLayout.setBackgroundColor(Color.argb(255, ((RgbLight) le).getrValue(), ((RgbLight) le).getgValue(), ((RgbLight) le).getbValue()));
                    viewHolder.tvName.setTextColor(Color.parseColor("#000000"));
                } else {
                    removeRGBLightView(le.getName());
                    viewHolder.relativeLayout.setBackgroundColor(getColor(R.color.lightOff));
                    viewHolder.tvName.setTextColor(Color.parseColor("#ffffff"));
                }
            }
            viewHolder.tvName.setText(le.getName());
            viewHolder.swBtn.setOnCheckedChangeListener(null);
            viewHolder.swBtn.setChecked(le.getState());
            final ViewHolder finalViewHolder = viewHolder;
            if (le.getType() == "white") {
                viewHolder.relativeLayout.setEnabled(false);
            } else {
                viewHolder.relativeLayout.setEnabled(true);
            }
            viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("11", le.getName() + finalViewHolder.tvName.getText());
                    light = setColoCycle(le.getName(), (Color.rgb(((RgbLight) le).getrValue(), ((RgbLight) le).getgValue(), ((RgbLight) le).getbValue())), le);

                }
            });
            viewHolder.relativeLayout.setOnTouchListener(onTouchListener);
            viewHolder.swBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (le.getType() == "white" || le.getType() == "smart") {
                        if (isChecked == true) {
                            le.setState(true);
                            addLightView(le.getName());
                            finalViewHolder.relativeLayout.setBackgroundColor(Color.argb(255, 255, 255, 255));
                            finalViewHolder.tvName.setTextColor(Color.parseColor("#000000"));
                        } else {
                            le.setState(false);
                            removeLightView(le.getName());
                            finalViewHolder.relativeLayout.setBackgroundColor(getColor(R.color.lightOff));
                            finalViewHolder.tvName.setTextColor(Color.parseColor("#ffffff"));
                        }
                    } else {
                        if (isChecked == true) {
                            le.setState(true);
                            addRGBLightView(le.getName(), Color.argb(255, ((RgbLight) le).getrValue(), ((RgbLight) le).getgValue(), ((RgbLight) le).getbValue()));
                            finalViewHolder.relativeLayout.setBackgroundColor(Color.argb(255, ((RgbLight) le).getrValue(), ((RgbLight) le).getgValue(), ((RgbLight) le).getbValue()));
                            finalViewHolder.tvName.setTextColor(Color.parseColor("#000000"));
                        } else {
                            le.setState(false);
                            removeRGBLightView(le.getName());
                            finalViewHolder.relativeLayout.setBackgroundColor(getColor(R.color.lightOff));
                            finalViewHolder.tvName.setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                    Log.i(TAG, SocketHelper.byte2hex(SocketHelper.attrsArray2D21D(getLightList.setLightList2Byte()), 160));
//                    notifyDataSetChanged();
                    if (TcpClient.getInstance() != null) {
//                        sendBtyesData2HW();
                        sendByteData2Controller();
                    }
                }

            });
            return viewItem;
        }
    }

    /**
     * addCycleView when initalized call this function
     */
    private void addCycleView(RelativeLayout relativeLayout) {
        int color = Color.rgb(r, g, b);
        Log.i("Click", "r:" + r + "g:" + g + "b:" + b);
        colorPickerView = new ColorPickerView(Control.this, Control.this, color);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        colorPickerView.setLayoutParams(params);
        relativeLayout.addView(colorPickerView);
    }

    /**
     * addTableView when initalized call this function
     */
    private void addTableView(RelativeLayout relativeLayout) {
        View vwColorTable = inflater.inflate(R.layout.vw_color_table, relativeLayout, false);
        gvTamplate = vwColorTable.findViewById(R.id.template_gridview);
        relativeLayout.addView(vwColorTable);
//        colorTemplateList = new GetColorTableList().getAllList();
        initializeTamplates(colorTemplateList);
    }

    /**
     * addSeekBarView when initalized call this function
     */
    private void addSeekBarView(RelativeLayout relativeLayout) {
        final View seekBarView = inflater.inflate(R.layout.seekbar_light, relativeLayout, false);
        relativeLayout.addView(seekBarView);
        final SeekBar seekBarRed = seekBarView.findViewById(R.id.seekBarRed);
        final SeekBar seekBarGreen = seekBarView.findViewById(R.id.seekBarGreen);
        final SeekBar seekBarBlue = seekBarView.findViewById(R.id.seekBarBlue);
        final SeekBar seekBarIntense = seekBarView.findViewById(R.id.seekBarIntense);
        final EditText inputRed = seekBarView.findViewById(R.id.red_input);
        final EditText inputGreen = seekBarView.findViewById(R.id.green_input);
        final EditText inputBlue = seekBarView.findViewById(R.id.blue_input);
        final EditText inputIntense = seekBarView.findViewById(R.id.intense_input);
        //设置
        RgbLight light = (RgbLight) adapter.light;
        r = light.getrValue();
        g = light.getgValue();
        b = light.getbValue();
        i = light.getIntenseValue();
        seekBarRed.setProgress(r);
        seekBarGreen.setProgress(g);
        seekBarBlue.setProgress(b);
        seekBarIntense.setProgress(i);
        inputRed.setText(String.valueOf(r));
        inputGreen.setText(String.valueOf(g));
        inputBlue.setText(String.valueOf(b));
        inputIntense.setText(String.valueOf(i));
        Drawable drawableR = getNewDrawable(Control.this, R.drawable.seekbar_thumb_pic, seekWidth, seekHeight);
        Drawable drawableG = getNewDrawable(Control.this, R.drawable.seekbar_thumb_pic, seekWidth, seekHeight);
        Drawable drawableB = getNewDrawable(Control.this, R.drawable.seekbar_thumb_pic, seekWidth, seekHeight);
        Drawable drawableI = getNewDrawable(Control.this, R.drawable.seekbar_thumb_pic, seekWidth, seekHeight);
        seekBarRed.setThumb(drawableR);
        seekBarBlue.setThumb(drawableG);
        seekBarGreen.setThumb(drawableB);
        seekBarIntense.setThumb(drawableI);
        //Red
        inputRed.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (notHandleAfterTextChangedEvent[0]) {
                    notHandleAfterTextChangedEvent[0] = false;

                    //光标置最后
                    CharSequence text = inputRed.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                    return;
                }
                if (s == null) {
                    return;
                }
                Integer value = 0;
                if (s.toString().trim().equals("")) {
                    notHandleAfterTextChangedEvent[0] = true;
                    inputRed.setText("");
                } else {
                    value = Integer.parseInt(s.toString());
                }
                if (null != value) {
                    if (value < 0) {
                        value = 0;
                    } else if (value > 255) {
                        value = 255;
                    }
                    notHandleAfterTextChangedEvent[0] = true;
                    inputRed.setText(Integer.toString(value));
                    try {
                        if (!changeFromSeekBar[0]) {
                            changeFromEditText[0] = true;
                            seekBarRed.setProgress(value);
                        } else {
                            changeFromSeekBar[0] = false;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Write Correct Number", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (!changeFromEditText[0]) {
                        changeFromSeekBar[0] = true;
                        inputRed.setText(String.valueOf(progress));
                        r = progress;
//                                tv.setTextColor(Color.rgb(r,g,b));
                        colorChanged(Color.rgb(r, g, b));
                        Log.i("Color", "Red r:" + r + ",g:" + g + " b:" + b);

                    } else {
                        changeFromEditText[0] = false;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Green
        inputGreen.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (notHandleAfterTextChangedEvent[1]) {
                    notHandleAfterTextChangedEvent[1] = false;

                    //光标置最后
                    CharSequence text = inputGreen.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                    return;
                }
                if (s == null) {
                    return;
                }
                Integer value = 0;
                if (s.toString().trim().equals("")) {
                    notHandleAfterTextChangedEvent[1] = true;
                    inputGreen.setText("");
                } else {
                    value = Integer.parseInt(s.toString());
                }
                if (null != value) {
                    if (value < 0) {
                        value = 0;
                    } else if (value > 255) {
                        value = 255;
                    }
                    notHandleAfterTextChangedEvent[1] = true;
                    inputGreen.setText(Integer.toString(value));
                    try {
                        if (!changeFromSeekBar[1]) {
                            changeFromEditText[1] = true;
                            seekBarGreen.setProgress(value);
                        } else {
                            changeFromSeekBar[1] = false;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Write Correct Number", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (!changeFromEditText[1]) {
                        changeFromSeekBar[1] = true;
                        inputGreen.setText(String.valueOf(progress));
                        int g = progress;
//                                tv.setTextColor(Color.rgb(r,g,b));
                        colorChanged(Color.rgb(r, g, b));
                        Log.i("Color", "Green r:" + r + ",g:" + g + " b:" + b);

                    } else {
                        changeFromEditText[1] = false;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Blue
        inputBlue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (notHandleAfterTextChangedEvent[2]) {
                    notHandleAfterTextChangedEvent[2] = false;

                    //光标置最后
                    CharSequence text = inputBlue.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                    return;
                }
                if (s == null) {
                    return;
                }
                Integer value = 0;
                if (s.toString().trim().equals("")) {
                    notHandleAfterTextChangedEvent[2] = true;
                    inputBlue.setText("");
                } else {
                    value = Integer.parseInt(s.toString());
                }
                if (null != value) {
                    if (value < 0) {
                        value = 0;
                    } else if (value > 255) {
                        value = 255;
                    }
                    notHandleAfterTextChangedEvent[2] = true;
                    inputBlue.setText(Integer.toString(value));
                    try {
                        if (!changeFromSeekBar[2]) {
                            changeFromEditText[2] = true;
                            seekBarBlue.setProgress(value);
                        } else {
                            changeFromSeekBar[2] = false;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Write Correct Number", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (!changeFromEditText[2]) {
                        changeFromSeekBar[2] = true;
                        inputBlue.setText(String.valueOf(progress));
                        b = progress;
//                                tv.setTextColor(Color.rgb(r,g,b));
                        colorChanged(Color.rgb(r, g, b));
                        Log.i("Color", "Blue r:" + r + ",g:" + g + " b:" + b);

                    } else {
                        changeFromEditText[2] = false;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Intense
        inputIntense.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (notHandleAfterTextChangedEvent[3]) {
                    notHandleAfterTextChangedEvent[3] = false;

                    //光标置最后
                    CharSequence text = inputIntense.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                    return;
                }
                if (s == null) {
                    return;
                }
                Integer value = 0;
                if (s.toString().trim().equals("")) {
                    notHandleAfterTextChangedEvent[3] = true;
                    inputIntense.setText("");
                } else {
                    value = Integer.parseInt(s.toString());
                }
                if (null != value) {
                    if (value < 0) {
                        value = 0;
                    } else if (value > 255) {
                        value = 255;
                    }
                    notHandleAfterTextChangedEvent[3] = true;
                    inputIntense.setText(Integer.toString(value));
                    try {
                        if (!changeFromSeekBar[3]) {
                            changeFromEditText[3] = true;
                            seekBarIntense.setProgress(value);
                        } else {
                            changeFromSeekBar[3] = false;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Write Correct Number", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        seekBarIntense.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (!changeFromEditText[3]) {
                        changeFromSeekBar[3] = true;
                        inputIntense.setText(String.valueOf(progress));
                        i = progress;
//                                tv.setTextColor(Color.rgb(r,g,b));
//                        colorChanged(Color.rgb(r, g, b));
                        intenseChanged(i);
                        Log.i("Color", "Intense r:" + r + ",g:" + g + " b:" + b);

                    } else {
                        changeFromEditText[3] = false;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    /**
     * Set ColorPickerView
     */
    private Light setColoCycle(String lightName, final int color, Light light) {
        final View hiddenView = findViewById(R.id.linearlayout1);
        if (null != hiddenView) {
            ViewGroup parent = (ViewGroup) hiddenView.getParent();
            parent.removeView(hiddenView);
        }
        final RelativeLayout rl = findViewById(R.id.rl);
        View vieww = inflater.inflate(R.layout.activity_color_cycle, rl, false);
        colorPickerView = new ColorPickerView(Control.this, Control.this, color);
        final RelativeLayout relativeLayout = vieww.findViewById(R.id.colorcycle);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        colorPickerView.setLayoutParams(params);
        relativeLayout.addView(colorPickerView);
        rl.addView(vieww);
        final TextView tv = vieww.findViewById(R.id.light_name);
        tv.setText(lightName);
        tv.setTextColor(color);

        //跳转
        final ImageView seekbarBtn = vieww.findViewById(R.id.seekbar_btn);
        final ImageView cycleBtn = vieww.findViewById(R.id.cycle_btn);
        final ImageView tableBtn = vieww.findViewById(R.id.table_btn);
        seekbarBtn.setOnTouchListener(onTouchListener);
        cycleBtn.setOnTouchListener(onTouchListener);
        tableBtn.setOnTouchListener(onTouchListener);
        cycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarBtn.setImageResource(R.drawable.seekbar_btn_off);
                cycleBtn.setImageResource(R.drawable.cycle_btn_on);
                tableBtn.setImageResource(R.drawable.table_btn_off);
                Log.i("Click", "cycleBarBtn Clicked");
                View hiddenView = findViewById(R.id.seekbarView);
                View hiddenView2 = findViewById(R.id.tableView);
                if (null != hiddenView) {
                    Log.i("Click", "into here");
                    ViewGroup parent = (ViewGroup) hiddenView.getParent();
                    parent.removeView(hiddenView);
                    addCycleView(relativeLayout);
                } else if (null != hiddenView2) {
                    Log.i("Click", "into here");
                    ViewGroup parent = (ViewGroup) hiddenView2.getParent();
                    parent.removeView(hiddenView2);
                    addCycleView(relativeLayout);
                }
            }
        });
        tableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarBtn.setImageResource(R.drawable.seekbar_btn_off);
                cycleBtn.setImageResource(R.drawable.cycle_btn_off);
                tableBtn.setImageResource(R.drawable.table_btn_on);
                Log.i("Click", "tableBtn Clicked");
                View hiddenView = findViewById(R.id.seekbarView);
                if (null != hiddenView) {
                    ViewGroup parent = (ViewGroup) hiddenView.getParent();
                    parent.removeView(hiddenView);
                    addTableView(relativeLayout);
                } else if (null != colorPickerView) {
                    ViewGroup parent = (ViewGroup) colorPickerView.getParent();
                    parent.removeView(colorPickerView);
                    colorPickerView = null;
                    addTableView(relativeLayout);
                }
            }
        });

        seekbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarBtn.setImageResource(R.drawable.seekbar_btn_on);
                cycleBtn.setImageResource(R.drawable.cycle_btn_off);
                tableBtn.setImageResource(R.drawable.table_btn_off);
                Log.i("Click", "seekBarBtn Clicked");
                View hiddenView2 = findViewById(R.id.tableView);
                if (null != colorPickerView) {
                    ViewGroup parent = (ViewGroup) colorPickerView.getParent();
                    parent.removeView(colorPickerView);
                    colorPickerView = null;
                    addSeekBarView(relativeLayout);
                } else if (null != hiddenView2) {
                    Log.i("Click", "into here");
                    ViewGroup parent = (ViewGroup) hiddenView2.getParent();
                    parent.removeView(hiddenView2);
                    addSeekBarView(relativeLayout);
                }


            }
        });


        return light;
    }

    //调用函数缩小图片
    public BitmapDrawable getNewDrawable(Activity context, int restId, int dstWidth, int dstHeight) {
        Bitmap Bmp = BitmapFactory.decodeResource(context.getResources(), restId);
        Bitmap bmp = Bmp.createScaledBitmap(Bmp, dstWidth, dstHeight, true);
        BitmapDrawable d = new BitmapDrawable(bmp);
        Bitmap bitmap = d.getBitmap();
        if (bitmap.getDensity() == Bitmap.DENSITY_NONE) {
            d.setTargetDensity(context.getResources().getDisplayMetrics());
        }
        return d;
    }

    //设置
//    Boolean notHandleAfterTextChangedEvent  = false;
//    Boolean changeFromSeekBar = false;
//    Boolean changeFromEditText = false;
//    private void setSeekbar(final SeekBar seekbar, final EditText editText, final String type){
//
//        editText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//                if (notHandleAfterTextChangedEvent) {
//                    notHandleAfterTextChangedEvent = false;
//
//                //光标置最后
//                CharSequence text = editText.getText();
//                if (text instanceof Spannable) {
//                    Spannable spanText = (Spannable) text;
//                    Selection.setSelection(spanText, text.length());
//                }
//                return;
//                }
//                if (s==null){return;}
//                Integer value = 0;
//                if(s.toString().trim().equals("")){
//                    notHandleAfterTextChangedEvent = true;
//                    editText.setText("");
//                }else{
//                    value = Integer.parseInt(s.toString());
//                }
//                if (null != value){
//                    if (value<0){
//                        value = 0;
//                    }else if(value>255){
//                        value=255;
//                    }
//                    notHandleAfterTextChangedEvent = true;
//                    editText.setText(Integer.toString(value));
//                    try{
//                        if (!changeFromSeekBar){
//                            changeFromEditText = true;
//                            seekbar.setProgress(value);
//                        }else{
//                            changeFromSeekBar = false;
//                        }
//                    }catch (NumberFormatException e){
//                        Toast.makeText(getApplicationContext(),"Write Correct Number",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(fromUser){
//                    if (!changeFromEditText){
//                        changeFromSeekBar = true;
//                        editText.setText(String.valueOf(progress));
//                        switch (type){
//                            case "r":
//                                r=progress;
//                                break;
//                            case "g":
//                                g=progress;
//                                break;
//                            case "b":
//                                b=progress;
//                                break;
//                            case "i":
//                                i=progress;
//                                break;
//                            default:
//                                break;
//                        }
//                        colorChanged(Color.rgb(r,g,b));
//                        Log.i("Color","Blue r:"+ r +",g:" + g +" b:"+b);
//                    }else{
//                        changeFromEditText = false;
//                    }
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//    }
    private class MyHandler extends Handler {
        private WeakReference<Control> mActivity;

        MyHandler(Control activity) {
            mActivity = new WeakReference<Control>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (mActivity != null) {
                switch (msg.what) {
                    case 1:
                        Log.i(TAG, "收到：" + msg.arg1);
                        if (msg.arg1 == 1) {
                            Toast.makeText(getApplicationContext(), "有控制权，操作者模式", Toast.LENGTH_SHORT).show();
                        } else if (msg.arg1 == 0) {
                            Toast.makeText(getApplicationContext(), "无控制权，观看者模式", Toast.LENGTH_SHORT).show();
                            rl.setOnClickListener(null);
                        }
                        break;
                    case 2:
                        /**
                         * 收到信号，转为LightList,刷新列表
                         */
//                        Log.i(TAG,"收到："+ msg.getData().getBundle("lightAttrs"));
                        Log.i(TAG, "收到：" + msg.getData().getByteArray("lightAttrs"));
//                        SocketHelper.attrsArray(attrs,msg.getData().getByteArray("lightAttrs"));
//                        getLightList.setByte2LightList(attrs);
//                        adapter.notifyDataSetChanged();
                        break;
                    case 5000:
//                        timer.cancel();
                        //tcpClient.closeSelf();
                        break;
                }
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            switch (mAction) {
                case "tcpClientReceiver":
                    Bundle bundle = intent.getBundleExtra("tcpClientReceiver");
                    byte b = bundle.getByte("permission");
                    byte[] attrsRcv = bundle.getByteArray("attrs");
                    SocketHelper.attrsArray(attrs, attrsRcv);
                    Log.i(TAG, "" + SocketHelper.byte2int(attrs[0][0]) + "," + SocketHelper.byte2int(attrs[0][1]) + "," + SocketHelper.byte2int(attrs[0][2]) + "," + SocketHelper.byte2int(attrs[0][3]) + "," + SocketHelper.byte2int(attrs[0][4]) + "," + SocketHelper.byte2int(attrs[0][5]));
                    Log.i(TAG, "" + SocketHelper.byte2int(b));

                    if (SocketHelper.byte2int(b) != permission) {
                        Message msgPermission = Message.obtain();
                        msgPermission.what = 1;
                        msgPermission.arg1 = SocketHelper.byte2int(b);
                        myHandler.sendMessage(msgPermission);
                    }
                    permission = SocketHelper.byte2int(b);
                    if (attrsRcv != attrsRvcPre) {
                        Log.i(TAG, "程序进入这里" + SocketHelper.byte2hex(attrsRcv, attrsRcv.length));

                        Message msgAttrs = Message.obtain();
                        msgAttrs.what = 2;
                        Bundle msgBundle = new Bundle();
                        msgBundle.putByteArray("lightAttrs", attrsRcv);
                        msgAttrs.setData(msgBundle);
                        myHandler.sendMessage(msgAttrs);
                    }
                    attrsRvcPre = attrsRcv;
                    break;
            }
        }
    }

    private void bindReceiver() {
        IntentFilter intentFilter = new IntentFilter("tcpClientReceiver");
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    /**
     * 发送转为btye[]的LightList ---> 智能顶灯
     */
    private void sendBtyesData2HW() {
        final TcpClient tcpClient = TcpClient.getInstance();
        if (null != tcpClient) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    tcpClient.sendByte(SocketHelper.attrsArray2D21D(getLightList.setLightList2Byte()));
                }
            });
        }
    }


    private void sendByteData2Controller() {
//        final byte[] attrs = new byte[8];
//        attrs[0] = (byte) 0xff;
//        attrs[1] = (byte) 0xff;
//        attrs[2] = (byte) r;
//        attrs[3] = (byte) g;
//        attrs[4] = (byte) b;
//        attrs[5] = (byte) i;
//        attrs[6] = (byte) 0x00;
//        attrs[7] = (byte) 0x02;
//        final TcpClient tcpClient = TcpClient.getInstance();
//        if (null != tcpClient) {
//            exec.execute(new Runnable() {
//                @Override
//                public void run() {
//                    tcpClient.sendByte(attrs);
//                }
//            });
//        }
        final TcpClient tcpClient = TcpClient.getInstance();
        if (null != tcpClient) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    tcpClient.sendByte(SocketHelper.attrsArray2D21D(getLightList.setLightList2ByteDisplayOnly(), 24));
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {
            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
            Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getBaseContext(), "onResume Accelerometer Started",
                Toast.LENGTH_SHORT).show();
        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isSupported(this)) {
            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {
            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
