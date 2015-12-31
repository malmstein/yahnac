package com.malmstein.yahnac.stories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.malmstein.yahnac.HNewsNavigationDrawerActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.data.Provider;
import com.malmstein.yahnac.injection.Inject;
import com.malmstein.yahnac.invite.AppInviter;
import com.malmstein.yahnac.model.OperationResponse;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.views.SnackBarView;
import com.malmstein.yahnac.views.drawer.ActionBarDrawerListener;
import com.malmstein.yahnac.views.drawer.NavigationDrawerHeader;
import com.novoda.notils.caster.Views;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class NewsActivity extends HNewsNavigationDrawerActivity implements StoryListener, ActionBarDrawerListener.Listener, NavigationDrawerHeader.Listener, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_INVITE = 0;
    private ViewPager headersPager;
    private SnackBarView snackbarView;
    private int croutonAnimationDuration;
    private int croutonBackgroundAlpha;
    private StoriesPagerAdapter storiesPagerAdapter;
    private Subscription subscription;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setupViews();
        showAppInviteIfNecessary();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        refreshHeader();
        setupViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                Inject.usageAnalytics().trackEvent(getString(R.string.analytics_event_app_invite_complete));
            }
        }
    }

    private void setupViews() {
        setupHeaders();
        setupTabs();
        setupSnackbar();
        setupAppBar();
    }

    private void setupHeaders() {
        headersPager = (ViewPager) findViewById(R.id.viewpager);
        storiesPagerAdapter = new StoriesPagerAdapter(getSupportFragmentManager());
        headersPager.setAdapter(storiesPagerAdapter);
        headersPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        Inject.usageAnalytics().trackPage(storiesPagerAdapter.getPageTitle(position).toString());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
    }

    private void setupTabs() {
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(headersPager);
        tabLayout.setOnTabSelectedListener(new StoryTabSelectedListener());
    }

    private void setupAppBar() {
        setHighLevelActivity();
        setTitle(getString(R.string.title_app));
    }

    private void setupSnackbar() {
        snackbarView = Views.findById(this, R.id.snackbar);
        croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);
    }

    private void showAppInviteIfNecessary() {
        AppInviter appInviter = Inject.appInviter();
        if (appInviter.shouldShow()) {
            setupGoogleClient();
            showAppInviteMessage();
        }
    }

    private void setupGoogleClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, this)
                .build();
    }

    private void showAppInviteMessage() {
        Snackbar.make(headersPager, R.string.app_invite, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.app_invite_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onInviteClicked();
                    }
                }).show();
    }

    private void onInviteClicked() {
        Inject.usageAnalytics().trackEvent(getString(R.string.analytics_event_app_invite_started));
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SimpleChromeCustomTabs.getInstance().connectTo(this);
        refreshHeader();
        trackCurrentPage();
    }

    @Override
    public void onPause() {
        SimpleChromeCustomTabs.getInstance().disconnectFrom(this);
        super.onPause();
    }

    private void trackCurrentPage() {
        Inject.usageAnalytics().trackPage(
                storiesPagerAdapter.getPageTitle(
                        headersPager.getCurrentItem()
                ).toString()
        );
    }

    @Override
    public void onShareClicked(Intent shareIntent) {
        Intent chooserIntent = Intent.createChooser(shareIntent, SHARE_DIALOG_DEFAULT_TITLE);
        startActivity(chooserIntent);
    }

    @Override
    public void onCommentsClicked(View v, Story story) {
        navigate().toComments(v, story);
    }

    @Override
    public void onCommentsClicked(Story story) {
        Inject.usageAnalytics().trackNavigateEvent(
                getString(R.string.analytics_event_view_comments_feed),
                story
        );
        navigate().toComments(story);
    }

    @Override
    public void onContentClicked(Story story) {
        Inject.usageAnalytics().trackNavigateEvent(
                getString(R.string.analytics_event_view_story_feed),
                story
        );
        DataPersister persister = Inject.dataPersister();
        persister.markStoryAsRead(story);
        navigate().toInnerBrowser(story);
    }

    @Override
    public void onExternalLinkClicked(Story story) {
        if (story.isHackerNewsLocalItem()) {
            Inject.usageAnalytics().trackNavigateEvent(
                    getString(R.string.analytics_event_view_comments_feed),
                    story
            );
            navigate().toComments(story);
        } else {
            Inject.usageAnalytics().trackNavigateEvent(
                    getString(R.string.analytics_event_view_external_url_feed),
                    story
            );
            navigate().toExternalBrowser(Uri.parse(story.getUrl()));
        }
    }

    @Override
    public void onBookmarkAdded(Story story) {
        Inject.usageAnalytics().trackBookmarkEvent(
                getString(R.string.analytics_event_add_bookmark_feed),
                story
        );
        DataPersister persister = Inject.dataPersister();
        showAddedBookmarkSnackbar(persister, story);
    }

    @Override
    public void onBookmarkRemoved(Story story) {
        Inject.usageAnalytics().trackBookmarkEvent(
                getString(R.string.analytics_event_remove_bookmark_feed),
                story
        );
        DataPersister persister = Inject.dataPersister();
        showRemovedBookmarkSnackbar(persister, story);
    }

    @Override
    public void onStoryVoteClicked(Story story) {
        Inject.usageAnalytics().trackVoteEvent(getString(R.string.analytics_event_vote), story);
        showSnackBarVoting();

        Provider provider = Inject.provider();
        subscription = provider
                .observeVote(story)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<OperationResponse>() {
                            @Override
                            public void onCompleted() {
                                if (!subscription.isUnsubscribed()) {
                                    subscription.unsubscribe();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Inject.crashAnalytics().logSomethingWentWrong("Provider - Vote: ", e);
                            }

                            @Override
                            public void onNext(OperationResponse status) {
                                if (status == OperationResponse.LOGIN_EXPIRED) {
                                    showLoginExpired();
                                } else {
                                    showSnackBarVoted();
                                }
                            }
                        }
                );
    }

    private void showSnackBarVoting() {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_voting))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .animating();
    }

    private void showSnackBarVoted() {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_voted))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .animating();
    }

    public void showLoginExpired() {
        snackbarView.showSnackBar(getResources().getText(R.string.login_expired_message))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withCustomTextClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                navigate().toLogin(null);
                            }
                        }, R.string.feed_snackbar_text_sign_in
                )
                .animating();
    }

    private void showAddedBookmarkSnackbar(final DataPersister persister, final Story story) {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_added_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbarView.hideCrouton();
                                removeBookmark(persister, story);
                            }
                        }
                )
                .animating();
    }

    private void showRemovedBookmarkSnackbar(final DataPersister persister, final Story story) {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_removed_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbarView.hideCrouton();
                                addBookmark(persister, story);
                            }
                        }
                )
                .animating();
    }

    private void removeBookmark(DataPersister persister, Story story) {
        Inject.usageAnalytics().trackBookmarkEvent(
                getString(R.string.analytics_event_remove_bookmark_feed),
                story
        );
        persister.removeBookmark(story);
        showRemovedBookmarkSnackbar(persister, story);
    }

    private void addBookmark(DataPersister persister, Story story) {
        Inject.usageAnalytics().trackBookmarkEvent(
                getString(R.string.analytics_event_add_bookmark_feed),
                story
        );
        persister.addBookmark(story);
        showAddedBookmarkSnackbar(persister, story);
    }

    @Override
    public void onNotImplementedFeatureSelected() {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_not_implemented))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .animating();
    }

    @Override
    public void onLoginClicked() {
        navigate().toLogin(Views.findById(this, R.id.view_drawer_header));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(headersPager, R.string.google_play_services_error, Snackbar.LENGTH_SHORT).show();
    }

    public class StoryTabSelectedListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            headersPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            String tag = storiesPagerAdapter.getTag(tab.getPosition());
            StoryFragment fragment = (StoryFragment) getSupportFragmentManager().findFragmentByTag(tag);
            fragment.scrollToTop();

        }
    }

}
