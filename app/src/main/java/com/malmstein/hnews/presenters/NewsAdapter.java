package com.malmstein.hnews.presenters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.malmstein.hnews.BuildConfig;
import com.malmstein.hnews.stories.TopStoriesFragment;

public class NewsAdapter extends TaggedFragmentStatePagerAdapter {

    private String[] categories = {"Top Stories" , "Show HN", "Ask HN", "Jobs"};

    private static final String TAG_TEMPLATE = BuildConfig.APPLICATION_ID + ".FEED_FRAGMENT#";

    public NewsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) new TopStoriesFragment();
    }

    @Override
    public String getTag(int position) {
        return TAG_TEMPLATE + position;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories[position];
    }
}
