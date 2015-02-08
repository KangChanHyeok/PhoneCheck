package com.kch.phonecheck;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainListAdapter extends ArrayAdapter<MainListVo>{

	ArrayList<MainListVo> list;
	Context context;
	public MainListAdapter(Context context, int resource, ArrayList<MainListVo> list) {
		super(context, resource, list);
		// TODO Auto-generated constructor stub
		this.list=list;
		this.context=context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
        View v = convertView;
        ImageView img;
        TextView txt_title;
        TextView txt_content;
        if (v == null) {	        	
            LayoutInflater vi = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.main_view, null);
        }
        img=(ImageView)v.findViewById(R.id.leftimg);
        txt_title=(TextView)v.findViewById(R.id.toptext);
        txt_content=(TextView)v.findViewById(R.id.bottomtext);
        
        img.setImageResource(list.get(position).getImage());
        txt_title.setText(list.get(position).getTitle());	        
        txt_content.setText(list.get(position).getContent());
        return v;
	}	
}
