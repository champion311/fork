package com.shosen.max.ui.circle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CircleFragmentAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments;

    public CircleFragmentAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public CircleFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (fragments != null) {
            return fragments[i];
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.length;
    }
}
