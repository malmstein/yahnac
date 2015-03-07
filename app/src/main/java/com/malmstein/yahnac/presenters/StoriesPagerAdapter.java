package com.malmstein.yahnac.presenters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.stories.AskHNFragment;
import com.malmstein.yahnac.stories.ShowHNFragment;
import com.malmstein.yahnac.stories.TopStoriesFragment;

public class StoriesPagerAdapter extends TaggedFragmentStatePagerAdapter {

    private String[] categories = {"Top Stories", "Newest", "Best", "Show HN", "Ask HN"};

    private static final String TAG_TEMPLATE = BuildConfig.APPLICATION_ID + ".FEED_FRAGMENT#";

    public StoriesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return TopStoriesFragment.from(TopStoriesFragment.QUERY.top);
            case 1:
                return TopStoriesFragment.from(TopStoriesFragment.QUERY.newest);
            case 2:
                return TopStoriesFragment.from(TopStoriesFragment.QUERY.best);
            case 3:
                return new ShowHNFragment();
            case 4:
                return new AskHNFragment();
            default:
                return new TopStoriesFragment();
        }
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
