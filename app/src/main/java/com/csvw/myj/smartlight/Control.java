package com.csvw.myj.smartlight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.List;

public class Control extends Activity implements OnColorChangedListener {
    final String TAG = "Control";
    private ImageView carImage;
    private RelativeLayout rl;
    private ListView lvLights;
    private GridView gvTamplate;
    private List<Light> lightsList;
    private List<Light> rgbLightsList;
    private List<Light> smartLightsList;
    private List<ColorTamplate> colorTamplateList;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private View view;
    private LayoutInflater inflater;
    LightsDataAdapter adapter;
    TemplatesDataAdapter templatesDataAdapter;
    ColorPickerView colorPickerView;

    Boolean[] notHandleAfterTextChangedEvent = {false, false, false, false};
    Boolean[] changeFromSeekBar = {false, false, false, false};
    Boolean[] changeFromEditText = {false, false, false, false};

    private static int r = 255, g = 255, b = 255, i = 255;

    //Thumb图片大小 P20 200
    private int seekWidth = (int) (MainActivity.density * 50+0.5f);
    private int seekHeight = (int) (MainActivity.density * 50+0.5f);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
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
        lightsList = new GetLightList().getWhiteLampList();
        rgbLightsList = new GetLightList().getRgbLampList();
        smartLightsList = new GetLightList().getSmartLightList();
        this.initialize(lightsList);
        imageView1 = findViewById(R.id.white_lamp_btn);
        imageView2 = findViewById(R.id.rgb_lamp_btn);
        imageView3 = findViewById(R.id.smartlight_btn);
        imageView4 = findViewById(R.id.setting_btn);
        imageView1.setOnClickListener(l);
        imageView2.setOnClickListener(l);
        imageView3.setOnClickListener(l);
        imageView4.setOnClickListener(l);
        //carImage
        carImage = findViewById(R.id.carImage);
        carImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("坐标","x: "+event.getX());
                Log.i("坐标","Y: "+event.getY());
                return true;
            }
        });

    }

    //单击事件监听器
    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.white_lamp_btn:
                    imageView1.setImageResource(R.drawable.white_light_btn_2_blue);
                    imageView2.setImageResource(R.drawable.rgb_light_btn_2);
                    imageView3.setImageResource(R.drawable.smartlight_btn_2);
                    imageView4.setImageResource(R.drawable.setting_btn_2);
                    initialize(lightsList);
                    hiddenView = findViewById(R.id.cycle_layout);
                    if (null != hiddenView) {
                        ViewGroup parent = (ViewGroup) hiddenView.getParent();
                        parent.removeView(hiddenView);
                        rl.addView(view);
                        initialize(lightsList);
                    }
                    break;
                case R.id.rgb_lamp_btn:
                    imageView1.setImageResource(R.drawable.white_light_btn_2);
                    imageView2.setImageResource(R.drawable.rgb_light_btn_2_blue);
                    imageView3.setImageResource(R.drawable.smartlight_btn_2);
                    imageView4.setImageResource(R.drawable.setting_btn_2);
                    View listView = findViewById(R.id.linearlayout1);
                    if (null != listView) {
                        initialize(rgbLightsList);
                    }
                    View hiddenView = findViewById(R.id.cycle_layout);
                    if (null != hiddenView) {
                        ViewGroup parent = (ViewGroup) hiddenView.getParent();
                        parent.removeView(hiddenView);
                        rl.addView(view);
                        initialize(rgbLightsList);
                    }
                    break;
                case R.id.smartlight_btn:
                    imageView1.setImageResource(R.drawable.white_light_btn_2);
                    imageView2.setImageResource(R.drawable.rgb_light_btn_2);
                    imageView3.setImageResource(R.drawable.smartlight_btn_2_blue);
                    imageView4.setImageResource(R.drawable.setting_btn_2);
                    Control.this.initialize(smartLightsList);
                    break;
                case R.id.setting_btn:
                    imageView1.setImageResource(R.drawable.white_light_btn_2);
                    imageView2.setImageResource(R.drawable.rgb_light_btn_2);
                    imageView3.setImageResource(R.drawable.smartlight_btn_2);
                    imageView4.setImageResource(R.drawable.setting_btn_2_blue);
                    break;
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

    private void initializeTamplates(List<ColorTamplate> colorTamplateList) {
        templatesDataAdapter = new TemplatesDataAdapter(this, colorTamplateList,this);
        this.gvTamplate.setAdapter(templatesDataAdapter);

    }
    /***
     * 添加LightView
     */
    public void addLightView(String string){
        switch(string){
            case "Reading Lamp":
                if (null!=reaadingLampView){
                }else{
                    reaadingLampView = new LightView(Control.this,R.drawable.reading_lamp,0,0);
                    frameLayout.addView(reaadingLampView);
                }
                break;
            case "Door Opener Lamp":
                if (null!=doorOpenerLampView){
                }else{
                    doorOpenerLampView = new LightView(Control.this,R.drawable.door_opener_lamp,0,221.5f);
                    frameLayout.addView(doorOpenerLampView);
                }
                break;
            case "Footwell Lamp_vo.":
                if (null!=footwellLampView){
                }else{
                    footwellLampView = new LightView(Control.this,R.drawable.footwell_lamp,0,305.13f);
                    frameLayout.addView(footwellLampView);
                }
                break;
            case "Makeup Lamp":
                if (null!=makeupLampView){
                }else{
                    makeupLampView = new LightView(Control.this,R.drawable.makeup_lamp,0,0);
                    frameLayout.addView(makeupLampView);
                }
                break;
            case "MIKO Lamp_up":
                if (null!=mikoLampUpView){
                }else{
                    mikoLampUpView = new LightView(Control.this,R.drawable.miko_lamp_up,0,346.1f);
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
    public void removeLightView(String string){
        switch(string){
            case "Reading Lamp":
                if (null!=reaadingLampView){
                    ViewGroup parent = (ViewGroup) reaadingLampView.getParent();
                    parent.removeView(reaadingLampView);
                    reaadingLampView = null;
                }
                break;
            case "Door Opener Lamp":
                if (null!=doorOpenerLampView){
                    ViewGroup parent = (ViewGroup) doorOpenerLampView.getParent();
                    parent.removeView(doorOpenerLampView);
                    doorOpenerLampView = null;
                }
                break;
            case "Footwell Lamp_vo.":
                if (null!=footwellLampView){
                    ViewGroup parent = (ViewGroup) footwellLampView.getParent();
                    parent.removeView(footwellLampView);
                    footwellLampView = null;
                }
                break;
            case "Makeup Lamp":
                if (null!=makeupLampView){
                    ViewGroup parent = (ViewGroup) makeupLampView.getParent();
                    parent.removeView(makeupLampView);
                    makeupLampView = null;
                }
                break;
            case "MIKO Lamp_up":
                if (null!=mikoLampUpView){
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
    public void addRGBLightView(String string,int color){
        switch(string){
            case "IP Decor Lamp":
                if (null!=ipDecorLampView){
                }else{
                    ipDecorLampView = new LightView(Control.this,R.drawable.ip_decor_lamp,0,255.69f,color);
                    frameLayout.addView(ipDecorLampView);
                }
                break;
            case "Starry Lamp":
                break;
            case "Door Decor Lamp_vo.":
                if (null!=doorDecorLampVo){
                }else{
                    doorDecorLampVo = new LightView(Control.this,R.drawable.door_decor_lamp_vo,0,252.43f,color);
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
    public void removeRGBLightView(String string){
        switch(string){
            case "IP Decor Lamp":
                if (null!=ipDecorLampView){
                    ViewGroup parent = (ViewGroup) ipDecorLampView.getParent();
                    parent.removeView(ipDecorLampView);
                    ipDecorLampView = null;
                }
                break;
            case "Door Decor Lamp_vo.":
                if (null!=doorDecorLampVo){
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
        TextView tv = findViewById(R.id.light_name);
        if (null != tv) {
            tv.setTextColor(color);
            light.setrValue((color & 0xff0000) >> 16);
            light.setgValue((color & 0x00ff00) >> 8);
            light.setbValue((color & 0x0000ff));
            String lightName = (String) tv.getText();
            switch(lightName){
                case "IP Decor Lamp":
                    if (ipDecorLampView!=null) {
                        ViewGroup parent = (ViewGroup) ipDecorLampView.getParent();
                        parent.removeView(ipDecorLampView);
                        ipDecorLampView = new LightView(Control.this, R.drawable.ip_decor_lamp, 0, 255.69f, color);
                        frameLayout.addView(ipDecorLampView);
                    }
                    break;
                case "Door Decor Lamp_vo.":
                    if (doorDecorLampVo!=null) {
                        ViewGroup parent = (ViewGroup) doorDecorLampVo.getParent();
                        parent.removeView(doorDecorLampVo);
                        doorDecorLampVo = new LightView(Control.this,R.drawable.door_decor_lamp_vo,0,252.43f,color);
                        frameLayout.addView(doorDecorLampVo);
                        doorOpenerLampView=null;
                    }
                    break;
            }
        }
        r = light.getrValue();
        g = light.getgValue();
        b = light.getbValue();
        Log.i("11", adapter.light.toString());
    }

    /**
     * OnTouch 效果
     * */
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
            if (le.getType() == "white" || le.getType() == "smart") {
                if (le.getState() == true) {
                    viewHolder.relativeLayout.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    viewHolder.tvName.setTextColor(Color.parseColor("#000000"));
                } else {
                    viewHolder.relativeLayout.setBackgroundColor(getColor(R.color.lightOff));
                    viewHolder.tvName.setTextColor(Color.parseColor("#ffffff"));
                }
            } else {
                if (le.getState() == true) {
                    viewHolder.relativeLayout.setBackgroundColor(Color.argb(255, ((RgbLight) le).getrValue(), ((RgbLight) le).getgValue(), ((RgbLight) le).getbValue()));
                    viewHolder.tvName.setTextColor(Color.parseColor("#000000"));
                } else {
                    viewHolder.relativeLayout.setBackgroundColor(getColor(R.color.lightOff));
                    viewHolder.tvName.setTextColor(Color.parseColor("#ffffff"));
                }
            }
            viewHolder.tvName.setText(le.getName());
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
                            addRGBLightView(le.getName(),Color.argb(255, ((RgbLight) le).getrValue(), ((RgbLight) le).getgValue(), ((RgbLight) le).getbValue()));
                            finalViewHolder.relativeLayout.setBackgroundColor(Color.argb(255, ((RgbLight) le).getrValue(), ((RgbLight) le).getgValue(), ((RgbLight) le).getbValue()));
                            finalViewHolder.tvName.setTextColor(Color.parseColor("#000000"));
                        } else {
                            le.setState(false);
                            removeRGBLightView(le.getName());
                            finalViewHolder.relativeLayout.setBackgroundColor(getColor(R.color.lightOff));
                            finalViewHolder.tvName.setTextColor(Color.parseColor("#ffffff"));
                        }
                    }

                }
            });
            return viewItem;
        }
    }

    /**
     * addCycleView when initalized call this function
     * */
    private void addCycleView(RelativeLayout relativeLayout){
        int color = Color.rgb(r, g, b);
        Log.i("Click", "r:"+r +"g:"+g+"b:"+b);
        colorPickerView = new ColorPickerView(Control.this, Control.this, color);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        colorPickerView.setLayoutParams(params);
        relativeLayout.addView(colorPickerView);
    }

    /**
     * addTableView when initalized call this function
     * */
    private void addTableView(RelativeLayout relativeLayout){
        View vwColorTable = inflater.inflate(R.layout.vw_color_table, relativeLayout, false);
        gvTamplate = vwColorTable.findViewById(R.id.template_gridview);
        relativeLayout.addView(vwColorTable);
        colorTamplateList = new GetColorTableList().getAllList();
        initializeTamplates(colorTamplateList);
    }

    /**
     * addSeekBarView when initalized call this function
     * */
    private void addSeekBarView(RelativeLayout relativeLayout){
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
                        colorChanged(Color.rgb(r, g, b));
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
                }else if (null != hiddenView2){
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
                }else if(null != colorPickerView){
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
                }else if(null != hiddenView2){
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

}
