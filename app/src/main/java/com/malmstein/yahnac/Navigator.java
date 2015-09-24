package com.malmstein.yahnac;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.malmstein.yahnac.bookmarks.BookmarksActivity;
import com.malmstein.yahnac.comments.CommentsActivity;
import com.malmstein.yahnac.comments.CommentsPresenter;
import com.malmstein.yahnac.login.LoginActivity;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.settings.SettingsActivity;
import com.malmstein.yahnac.stories.NewsActivity;
import com.malmstein.yahnac.story.CustomTabActivityHelper;
import com.malmstein.yahnac.story.StoryActivity;
import com.malmstein.yahnac.views.transitions.TransitionHelper;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class Navigator {

    private final HNewsActivity activity;

    private CompositeSubscription mSubscriptions;
    private CustomTabActivityHelper customTabActivityHelper;

    public Navigator(HNewsActivity activity) {
        this.activity = activity;

        customTabActivityHelper = new CustomTabActivityHelper();
        mSubscriptions = new CompositeSubscription();
    }

    public void onStart() {
        customTabActivityHelper.bindCustomTabsService(activity);
    }

    public void onStop() {
        customTabActivityHelper.unbindCustomTabsService(activity);
    }

    protected boolean isOnline() {
        return activity.isOnline();
    }

    public void toExternalBrowser(Uri articleUri) {
        if (isOnline()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(articleUri);

            ActivityCompat.startActivity(activity, browserIntent, null);
        }
    }

    public void toInnerBrowser(final Story story) {
        if (isOnline()) {
            customTabActivityHelper.mayLaunchUrl(Uri.parse(story.getUrl()), null, null);
            mSubscriptions.add(prepareCustomTabsBuilder(story)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<CustomTabsIntent.Builder>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(CustomTabsIntent.Builder builder) {
                            openCustomTabsIfPossible(story, builder);
                        }
                    }));
        }
    }

    private Observable<CustomTabsIntent.Builder> prepareCustomTabsBuilder(final Story story) {
        return Observable.create(new Observable.OnSubscribe<CustomTabsIntent.Builder>() {
            @Override
            public void call(Subscriber<? super CustomTabsIntent.Builder> subscriber) {
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

                int color = activity.getResources().getColor(R.color.orange);
                intentBuilder.setToolbarColor(color);

                String shareLabel = activity.getString(R.string.action_comments);
                Bitmap icon = BitmapFactory.decodeResource(activity.getResources(),
                        R.drawable.ic_comment);
                PendingIntent pendingIntent = createPendingIntent(story);
                intentBuilder.setActionButton(icon, shareLabel, pendingIntent);

                intentBuilder.setCloseButtonIcon(
                        BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_arrow_back));

                subscriber.onNext(intentBuilder);
                subscriber.onCompleted();
            }
        });
    }

    private PendingIntent createPendingIntent(Story story) {
        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentsActivity.ARG_STORY, story);
        return PendingIntent.getActivity(activity.getApplicationContext(), 0, commentIntent, 0);
    }

    private void openCustomTabsIfPossible(final Story story, CustomTabsIntent.Builder intentBuilder) {

        CustomTabActivityHelper.openCustomTab(
                activity, intentBuilder.build(),
                Uri.parse(story.getUrl()), new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        Intent articleIntent = new Intent(activity, StoryActivity.class);
                        articleIntent.putExtra(StoryActivity.ARG_STORY, story);
                        ActivityCompat.startActivity(activity, articleIntent, null);
                    }
                });
    }

    public void toComments(View v, Story story) {
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, new Pair<>(v, CommentsPresenter.VIEW_NAME_HEADER_TITLE));

        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentsActivity.ARG_STORY, story);

        ActivityCompat.startActivity(activity, commentIntent, activityOptions.toBundle());
    }

    public void toComments(Story story) {
        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentsActivity.ARG_STORY, story);

        ActivityCompat.startActivity(activity, commentIntent, null);
    }

    public void toSettings() {
        Intent settingsIntent = new Intent(activity, SettingsActivity.class);
        ActivityCompat.startActivity(activity, settingsIntent, null);
    }

    public void toNews() {
        Intent newsIntent = new Intent(activity, NewsActivity.class);
        newsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityCompat.startActivity(activity, newsIntent, null);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }

    public void toBookmarks() {
        Intent bookmarksIntent = new Intent(activity, BookmarksActivity.class);
        ActivityCompat.startActivity(activity, bookmarksIntent, null);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }

    public void toLogin(View v) {

        final android.util.Pair[] pairs =
                TransitionHelper.createSafeTransitionParticipants
                        (activity,
                                false,
                                new android.util.Pair<>(v, LoginActivity.VIEW_TOOLBAR_TITLE));

        if ((v != null) && (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)) {
            ActivityOptions sceneTransitionAnimation = ActivityOptions
                    .makeSceneTransitionAnimation(activity, pairs);

            final Bundle transitionBundle = sceneTransitionAnimation.toBundle();
            Intent loginLollipopIntent = new Intent(activity, LoginActivity.class);
            ActivityCompat.startActivity(activity, loginLollipopIntent, transitionBundle);
        } else {
            Intent loginIntent = new Intent(activity, LoginActivity.class);
            ActivityCompat.startActivity(activity, loginIntent, null);
            activity.overridePendingTransition(0, 0);
        }

    }

}
