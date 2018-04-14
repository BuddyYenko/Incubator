package com.example.s215087038.incubator.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = "SectionsPagerAdapter";
    private final Context context;
    List<FragmentContainer> fragmentList = null;
    Map<String, FragmentContainer> fragmentMap = null;

    private class FragmentContainer {
        public Fragment fragment;
        public int titleId;

        public FragmentContainer(Fragment fragment, int titleId) {
            this.fragment = fragment;
            this.titleId = titleId;
        }
    }

    public static void setFragmentArgs(Fragment fragment, Bundle args) {
        fragment.setArguments(args);
    }

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.fragmentList = new ArrayList();
        this.fragmentMap = new HashMap();
    }

    public void addFragment(Fragment fragment, int titleId, String tag) {
        int pos = 0;
        if (!this.fragmentList.isEmpty()) {
            pos = this.fragmentList.size();
        }
        FragmentContainer frContainer = new FragmentContainer(fragment, titleId);
        this.fragmentList.add(pos, frContainer);
        this.fragmentMap.put(tag, frContainer);
        Log.d(TAG, "ADDED:--->" + this.fragmentMap.get(tag));
    }

    public Fragment findFragmentByTag(String tag) {
        if (this.fragmentMap.containsKey(tag)) {
            return ((FragmentContainer) this.fragmentMap.get(tag)).fragment;
        }
        return null;
    }

    public int getCount() {
        return this.fragmentList.size();
    }

    public int getFragmentPosition(String tag) {
        return this.fragmentList.indexOf((FragmentContainer) this.fragmentMap.get(tag));
    }

    public Fragment getItem(int i) {
        return ((FragmentContainer) this.fragmentList.get(i)).fragment;
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    public CharSequence getPageTitle(int position) {
        return this.context.getString(((FragmentContainer) this.fragmentList.get(position)).titleId);
    }

    public void insertFragment(Fragment fragment, int position, int titleId, String tag) {
        FragmentContainer insTo = new FragmentContainer(fragment, titleId);
        this.fragmentList.set(position, insTo);
        this.fragmentMap.put(tag, insTo);
    }

    public void removeAllFragments() {
        boolean shouldNotify = getCount() > 0;
        this.fragmentList.clear();
        this.fragmentMap.clear();
        if (shouldNotify) {
            notifyDataSetChanged();
        }
    }

    public void removeFragment(String tag) {
        FragmentContainer pos = (FragmentContainer) this.fragmentMap.get(tag);
        if (pos != null) {
            this.fragmentList.remove(pos);
            this.fragmentMap.remove(tag);
            notifyDataSetChanged();
        }
    }

    public void replaceFragment(Fragment fragment, String tag2replace) {
        FragmentContainer newfrCont = new FragmentContainer(fragment, ((FragmentContainer) this.fragmentMap.get(tag2replace)).titleId);
        this.fragmentList.set(this.fragmentList.indexOf((FragmentContainer) this.fragmentMap.get(tag2replace)), newfrCont);
        this.fragmentMap.put(tag2replace, newfrCont);
        notifyDataSetChanged();
    }
}
