package com.app.codeweb.codeweb.LessonActivity;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LesAdapter extends ArrayAdapter<SrlLesson> {

    Context context;
    private List<SrlLesson> data;
    int lvl;
    LayoutInflater inflater;

    public  LesAdapter (Context context, int resource, ArrayList<SrlLesson> obj, int lvl){
      super(context, resource, obj);
        this.context = context;
        this.lvl = lvl;
        this.data = obj;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SrlLesson getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_layout, null);
        }

        TextView lsn_title = (TextView) view.findViewById(R.id.lessontext);
        TextView lsn_lesson = (TextView) view.findViewById(R.id.questiontext);
        TextView lsn_number = (TextView) view.findViewById(R.id.numbertext);
        TextView lock = (TextView) view.findViewById(R.id.tvlock);
        ImageView lsn_img = (ImageView) view.findViewById(R.id.imageview);
        SrlLesson srldata = data.get(i);

        if(i >= lvl && lvl != 0){
            lock.setVisibility(View.VISIBLE);
        }else{
            lock.setVisibility(View.INVISIBLE);
        }
        lsn_title.setText(srldata.lsn_title);
        lsn_lesson.setText("Lesson " + srldata.lsn_no);
        lsn_number.setText(srldata.lsn_no + "/" + data.size());
        Picasso.with(context)
                .load(srldata.lsn_backpanel)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_sys_download)
                .into(lsn_img);

        return view;
    }
}
