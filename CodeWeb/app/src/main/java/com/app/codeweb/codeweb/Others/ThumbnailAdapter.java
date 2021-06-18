package com.app.codeweb.codeweb.Others;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ThumbnailAdapter extends ArrayAdapter<SrlLesson> {

    Context context;
    private List<SrlLesson> data;
    int lvl;
    LayoutInflater inflater;

    public ThumbnailAdapter(Context context, int resource, ArrayList<SrlLesson> obj){
      super(context, resource, obj);
        this.context = context;
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
            view = inflater.inflate(R.layout.custom_thumbnail_layout, null);
        }

        TextView lsn_course = (TextView) view.findViewById(R.id.questiontext);
        TextView title = (TextView) view.findViewById(R.id.title);
        ImageView lsn_img = (ImageView) view.findViewById(R.id.imageview);
        SrlLesson srldata = data.get(i);

            if(!srldata.lsn_video.isEmpty()){
                lsn_course.setText(srldata.lsn_course);
                title.setText(srldata.lsn_title);
                Bitmap tb = ThumbnailUtils.createVideoThumbnail(srldata.lsn_video, MediaStore.Video.Thumbnails.MICRO_KIND);
                lsn_img.setImageBitmap(tb);
            }

        return view;
    }
}
