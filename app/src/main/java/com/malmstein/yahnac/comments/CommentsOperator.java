package com.malmstein.yahnac.comments;

import android.content.Intent;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.injection.Inject;
import com.malmstein.yahnac.model.Story;
import com.novoda.notils.exception.DeveloperError;

public class CommentsOperator {

    private final HNewsActivity activity;
    private final Story story;
    private DataPersister persister;

    public CommentsOperator(HNewsActivity activity) {
        this.activity = activity;
        this.story = getStory();
        this.persister = Inject.dataPersister();
    }

    private Story getStory() {
        if (activity.getIntent().getExtras().containsKey(CommentsFragment.ARG_STORY)) {
            return (Story) activity.getIntent().getExtras().getSerializable(CommentsFragment.ARG_STORY);
        } else {
            throw new DeveloperError("Missing argument");
        }
    }

    public void onResume() {
        trackAnalytics(R.string.analytics_page_comments);
    }

    public void onArticleSelected() {
        trackAnalytics(R.string.analytics_event_view_story_comments);
        activity.navigate().toInnerBrowser(story);
        activity.finish();
    }

    public void onBookmarkUnselected() {
        trackAnalytics(R.string.analytics_event_remove_bookmark_comments);
        persister.removeBookmark(story);
    }

    public void onBookmarkSelected() {
        trackAnalytics(R.string.analytics_event_add_bookmark_comments);
        persister.addBookmark(story);
    }

    public void onShareArticle() {
        trackAnalytics(R.string.analytics_event_share_story);
        Intent chooserIntent = Intent.createChooser(
                story.createShareIntent(), null);
        activity.startActivity(chooserIntent);
    }

    private void trackAnalytics(int actionResource) {
        Inject.usageAnalytics().trackShareEvent(
                activity.getString(actionResource),
                story);
    }

    public void retrieveComments() {

    }
}
