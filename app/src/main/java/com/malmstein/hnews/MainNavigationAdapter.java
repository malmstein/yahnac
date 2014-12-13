package com.malmstein.hnews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainNavigationAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"News", "Comments", "Show", "Ask"};

    public MainNavigationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ItemListFragment();
        } else {
            return DefaultCardFragment.newInstance(position);
        }
    }
}