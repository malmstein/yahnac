package com.malmstein.hnews.presenters;

import android.app.Fragment;
import android.app.FragmentManager;

import com.malmstein.hnews.BuildConfig;
import com.malmstein.hnews.stories.TopStoriesFragment;

public class NewsAdapter extends TaggedFragmentStatePagerAdapter {

    private static final String TAG_TEMPLATE = BuildConfig.APPLICATION_ID + ".FEED_FRAGMENT#";
    private static final int HEADERS = 4;

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
        return HEADERS;
    }
}
