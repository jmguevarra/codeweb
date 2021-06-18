package com.app.codeweb.codeweb.Admin_Activity.TabCom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.app.codeweb.codeweb.Admin_Activity.AdminPerformance;
import com.app.codeweb.codeweb.R;
import com.app.codeweb.codeweb.Admin_Activity.AdminUsers;
import com.app.codeweb.codeweb.Admin_Activity.AdminSections;


public class MyAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] titles ={"A","B","C"};
    int[] icon = new int[]{R.drawable.performance,R.drawable.students,R.drawable.admin};
    private int heightIcon;

    public MyAdapter(FragmentManager fm, Context c){
        super(fm);
        mContext = c;
        double scale = c.getResources().getDisplayMetrics().density;
        heightIcon=(int)(40*scale+0.6f);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag= null;

        if(position ==0){
          frag = new AdminPerformance();
        }else if(position == 1){
            frag = new AdminSections();
        }else if(position == 2){
            frag = new AdminUsers();
        }

        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    public CharSequence getPageTitle(int position){
        Drawable d = mContext.getResources().getDrawable(icon[position]);
        d.setBounds(0,0,heightIcon,heightIcon);
        ImageSpan is = new ImageSpan(d);

        SpannableString sp = new SpannableString(" ");
        sp.setSpan(is,0,sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sp;
    }

}