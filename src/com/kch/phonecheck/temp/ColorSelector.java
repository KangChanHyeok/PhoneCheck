package com.kch.phonecheck.temp;

import java.util.ArrayList;

import com.kch.phonecheck.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


public class ColorSelector extends ListPreference{
	public final String PREPERENCE_KEY_LIST = "preList"; 
    private CustomListPreferenceAdapter customListPreferenceAdapter = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private CharSequence[] entries;
    private CharSequence[] entryValues;
    private final int[] entryImgId = {
    		Color.WHITE,
    		Color.RED,
    		Color.parseColor("#bada55"),
    		Color.BLUE,
    		Color.BLACK
    };
    private ArrayList<RadioButton> rButtonList;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public ColorSelector(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        rButtonList = new ArrayList<RadioButton>();
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = prefs.edit();
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder)
    {
    	
        entries = getEntries();
        entryValues = getEntryValues();

        if (entries == null || entryValues == null || entries.length != entryValues.length )
        {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array which are both the same length");
        }


        customListPreferenceAdapter = new CustomListPreferenceAdapter(mContext);

        builder.setAdapter(customListPreferenceAdapter, this);
        builder.setPositiveButton( null, null );
    }

    private class CustomListPreferenceAdapter extends BaseAdapter
    {        
        public CustomListPreferenceAdapter(Context context)
        {

        }

        public int getCount()
        {
            return entries.length;
        }

        public Object getItem(int position)
        {
            return position;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {  
            View row = convertView;
            CustomHolder holder = null;

            if(row == null)
            {                                                                   
                row = mInflater.inflate(R.layout.custom_list, parent, false);
                holder = new CustomHolder(row, position);
                row.setTag(holder);
                
                // do whatever you need here, for me I wanted the last item to be greyed out and unclickable
                row.setClickable(true);
                row.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                    	
                        for(RadioButton rb : rButtonList)
                        {
                            if(rb.getId() != position)
                                rb.setChecked(false);
                            else
                            	rb.setChecked(true);
                        }

                        int index = position;
                        int value = Integer.valueOf((String) entryValues[index]);
                        editor.putString(PREPERENCE_KEY_LIST , value+"");
                        editor.commit();
                        Dialog mDialog = getDialog();
                        mDialog.dismiss();
                    }
                });
            }
            
            return row;
        }

        class CustomHolder
        {
        	private LinearLayout img = null;
            private TextView text = null;
            private RadioButton rButton = null;

            CustomHolder(View row, final int position)
            {    
            	img = (LinearLayout)row.findViewById(R.id.custom_layout);
            	img.setBackgroundColor(entryImgId[position]);
 
                text = (TextView)row.findViewById(R.id.custom_text);
                text.setText(entries[position]);
                rButton = (RadioButton)row.findViewById(R.id.custom_button);
                rButton.setId(position);
                int selectedPos = Integer.parseInt(prefs.getString(PREPERENCE_KEY_LIST , "0"));
                
                if(position == selectedPos ){
                	rButton.setChecked(true);
                }                	
                

                // also need to do something to check your preference and set the right button as checked

                rButtonList.add(rButton);
                rButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        if(isChecked)
                        {
                        	
                            for(RadioButton rb : rButtonList)
                            {
                                if(rb != buttonView)
                                    rb.setChecked(false);                                
                            }

                            int index = buttonView.getId();
                            int value = Integer.valueOf((String) entryValues[index]);
                            editor.putString(PREPERENCE_KEY_LIST , value+"");                            
                            editor.commit();                            
                            Dialog mDialog = getDialog();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        }
    }
}
