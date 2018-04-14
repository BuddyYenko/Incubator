package com.example.s215087038.incubator.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.s215087038.incubator.R;
import com.zktechnology.android.zkbiosecurity.R;
import com.zktechnology.android.zkbiosecurity.util.SharedPreferencesManager;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private OnGoHomeListener callCack;
    private Context context;
    private List<View> views;

    public interface OnGoHomeListener {
        void goNextActivity();
    }

    public ViewPagerAdapter(List<View> views, Context context) {
        this.views = views;
        this.context = context;
        this.callCack = (OnGoHomeListener) context;
    }

    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) this.views.get(arg1));
    }

    public void finishUpdate(View arg0) {
    }

    public int getCount() {
        if (this.views != null) {
            return this.views.size();
        }
        return 0;
    }

    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView((View) this.views.get(arg1), 0);
        ((Button) arg0.findViewById(R.id.go_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SharedPreferencesManager.setIsFrist(ViewPagerAdapter.this.context, false);
                ViewPagerAdapter.this.callCack.goNextActivity();
            }
        });
        return this.views.get(arg1);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    public Parcelable saveState() {
        return null;
    }

    public void startUpdate(View arg0) {
    }
}
