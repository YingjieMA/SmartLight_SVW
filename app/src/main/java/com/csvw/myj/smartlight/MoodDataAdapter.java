package com.csvw.myj.smartlight;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * @ClassName: MoodDataAdapter
 * @Description: Mood GridView Adapter
 * @Author: MYJ
 * @CreateDate: 2020/4/27 11:10
 */
public class MoodDataAdapter extends BaseAdapter {
    private Context context;
    private List<MoodTemplate> dataList;
    private OnColorChangedListener mListener;

    public MoodDataAdapter(Context context, List<MoodTemplate> dataList,OnColorChangedListener l) {
        this.mListener = l;
        this.context = context;
        this.dataList = dataList;
    }

    public MoodDataAdapter() {
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.dataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return this.dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return this.dataList.get(position).getId();
    }
    private class ViewHolder {
        private TextView name;
        private ImageView imgPicture;
        private ImageView imgBorder;
        private FrameLayout frameLayout;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View itemView = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            itemView = inflater.inflate(R.layout.gridview_item_mood, null);
            viewHolder = new ViewHolder();
            viewHolder.name= (TextView) itemView
                    .findViewById(R.id.mood_templatename);
            viewHolder.imgPicture= (ImageView) itemView
                    .findViewById(R.id.mood_templateimage);
            viewHolder.frameLayout = itemView.findViewById(R.id.mood_gird_list_item);
            viewHolder.imgBorder = itemView.findViewById(R.id.mood_border);
            itemView.setTag(viewHolder);

        } else {
            itemView = convertView;
            viewHolder = (ViewHolder) itemView.getTag();
        }
        MoodTemplate item = this.dataList.get(position);
        viewHolder.name.setText(item.getName());
        switch (item.getName()){
            case "Desire":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(193,230,255));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border_desire);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_desire);
                viewHolder.name.setTextColor(ContextCompat.getColor(context,R.color.desire));
                break;
            case "Eternity":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(205,255,191));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border_eternity);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_eternity);
                viewHolder.name.setTextColor(ContextCompat.getColor(context,R.color.eternity));
                break;
            case "Euphoria":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(245,214,216));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border_euphoria);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_euphoria);
                viewHolder.name.setTextColor(ContextCompat.getColor(context,R.color.euphoria));
                break;
            case "Infinity":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(255,255,147));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border_infinity);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_infinity);
                viewHolder.name.setTextColor(ContextCompat.getColor(context,R.color.infinity));
                break;
            case "Vitality":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(242,242,242));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border_vitality);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_vitality);
                viewHolder.name.setTextColor(ContextCompat.getColor(context,R.color.vitality));
                break;
            case "Freedom":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(225,193,255));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border_freedom);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_freedom);
                viewHolder.name.setTextColor(ContextCompat.getColor(context,R.color.freedom));
                break;
            default:
                break;
        }
        viewHolder.frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setScaleX((float) 0.95);
                        v.setScaleY((float) 0.95);
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
        });
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.moodChanged((String) finalViewHolder.name.getText());
            }
        });
        viewHolder.frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.moodSave((String) finalViewHolder.name.getText());
                return true;
            }
        });
        return itemView;
    }
}
