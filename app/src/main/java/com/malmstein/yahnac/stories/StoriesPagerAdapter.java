package com.malmstein.yahnac.stories;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.views.recyclerview.adapter.TaggedFragmentStatePagerAdapter;

public class StoriesPagerAdapter extends TaggedFragmentStatePagerAdapter {

    private static final String TAG_TEMPLATE = BuildConfig.APPLICATION_ID + ".STORY_FRAGMENT#";

    private String[] categories = {"Top Stories", "Newest", "Best", "Show HN", "Ask HN", "Jobs"};

    public StoriesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
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
            case 5:
                return new JobsHNFragment();
            default:
                return new JobsHNFragment();
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
