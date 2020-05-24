package com.csvw.myj.smartlight;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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

    public MoodDataAdapter(Context context, List<MoodTemplate> dataList) {
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
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_desire);
                break;
            case "Eternity":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(205,255,191));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_eternity);
                break;
            case "Euphoria":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(245,214,216));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_euphoria);
                break;
            case "Infinity":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(255,255,147));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_infinity);
                break;
            case "Vitality":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(242,242,242));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_vitality);
                break;
            case "Freedom":
                viewHolder.imgPicture.setBackgroundColor(Color.rgb(225,193,255));
                viewHolder.imgBorder.setImageResource(R.drawable.mood_grid_view_border);
                viewHolder.imgPicture.setImageResource(R.drawable.mood_freedom);
                break;
            default:
                break;
        }
        return itemView;
    }
}
