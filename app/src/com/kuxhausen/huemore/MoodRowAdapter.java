package com.kuxhausen.huemore;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MoodRowAdapter extends ArrayAdapter<MoodRow> {

	public MoodRowAdapter(Activity context,
			ArrayList<MoodRow> objects) {
		super(context,R.layout.edit_mood_row_dialog);
		this.activity = context;
        this.list = objects;
	}
	
	private final Activity activity;
    private final ArrayList<MoodRow> list;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder view;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.edit_mood_row_dialog, null);

            // Hold the view objects in an object, that way the don't need to be "re-  finded"
            view = new ViewHolder();
            
            view.state_name= (EditText) rowView.findViewById(R.id.stateNameTextView);
            view.state_color = (ImageView) rowView.findViewById(R.id.stateColorButton);

            rowView.setTag(view);
        } else {
            view = (ViewHolder) rowView.getTag();
        }

        /** Set data to your Views. */
        MoodRow item = list.get(position);
        view.state_name.setText(item.name);
        
        ColorDrawable cd = new ColorDrawable(item.color);
        cd.setAlpha(255);
        view.state_color.setImageDrawable(cd);

        return rowView;
    }

    protected static class ViewHolder{
        protected EditText state_name;
        protected ImageView state_color;
    }
}


