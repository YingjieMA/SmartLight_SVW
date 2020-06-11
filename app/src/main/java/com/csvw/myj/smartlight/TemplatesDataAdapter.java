package com.csvw.myj.smartlight;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;


import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TemplatesDataAdapter extends BaseAdapter {
	private Context context;
	private List<ColorTemplate> dataList;
	private OnColorChangedListener mListener;
	public TemplatesDataAdapter(Context context, List<ColorTemplate> dataList, OnColorChangedListener l) {
		mListener = l;
		this.context = context;
		this.dataList = dataList;
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
			itemView = inflater.inflate(R.layout.gridview_item_color, null);
			viewHolder = new ViewHolder();
			viewHolder.name= (TextView) itemView
					.findViewById(R.id.templatename);
			viewHolder.imgPicture= (ImageView) itemView
					.findViewById(R.id.templateimage);
			viewHolder.frameLayout = itemView.findViewById(R.id.gird_list_item);
			viewHolder.imgBorder = itemView.findViewById(R.id.border);
			itemView.setTag(viewHolder);
		
		} else {
			itemView = convertView;
			viewHolder = (ViewHolder) itemView.getTag();
		}
		viewHolder.frameLayout.setOnTouchListener(new View.OnTouchListener() {
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
		});
		final ColorTemplate item = this.dataList.get(position);
		viewHolder.frameLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.colorChanged(Color.parseColor(item.getLightColor()));
			}
		});

		viewHolder.name.setText(item.getName());
		Log.i("object",item.getAppColor());
		viewHolder.imgPicture.setBackgroundColor(Color.parseColor(item.getAppColor()));
		if (dataList.get(position).getType() == "VW10"){
			viewHolder.imgBorder.setImageResource(R.drawable.grid_view_border);
		}
		return itemView;
	}

}
