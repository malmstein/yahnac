package com.malmstein.hnews.presenters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.malmstein.hnews.NewsListFragment;
import com.malmstein.hnews.model.Item;

public class MainNavigationAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"News", "Show", "Ask"};

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
        switch (position){
            case 0:
                return NewsListFragment.from(Item.TYPE.story);
            case 1:
                return NewsListFragment.from(Item.TYPE.show);
            case 2:
                return NewsListFragment.from(Item.TYPE.ask);
            default:
                return NewsListFragment.from(Item.TYPE.story);
        }
    }
}