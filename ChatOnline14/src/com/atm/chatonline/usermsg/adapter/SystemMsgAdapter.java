package com.atm.chatonline.usermsg.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atm.chatonline.usermsg.bean.Notification;
import com.example.studentsystem01.R;

public class SystemMsgAdapter extends ArrayAdapter<Notification> {

	private List<Notification> list;
	private Context context;
	private int resId;
	
	public SystemMsgAdapter(Context context, int resource,
			List<Notification> objects) {
		super(context, resource,objects);
		this.list=objects;
		this.context=context;
		this.resId=resource;	
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Notification notify=getItem(position);
		ViewHolder viewHolder;
		
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(resId, null);
			viewHolder=new ViewHolder();
			viewHolder.title=(TextView) convertView.findViewById(R.id.systemmsg_title);
			viewHolder.time=(TextView) convertView.findViewById(R.id.systemmsg_time);
			viewHolder.content=(TextView) convertView.findViewById(R.id.systemmsg_content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.title.setText(notify.getTitle());
		viewHolder.content.setText(notify.getContent());
		viewHolder.time.setText(notify.getTime());
		return convertView;
	}

}
class ViewHolder{
 TextView title,time,content;
}

