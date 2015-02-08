package com.malmstein.hnews.data;

import android.content.ContentValues;

import com.malmstein.hnews.model.Story;

import java.util.Vector;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func5;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class DataRepository {

    private final BehaviorSubject<Vector<ContentValues>> storiesSubject;
    private final BehaviorSubject<Integer> storySubject;
    private final BehaviorSubject<Integer> commentsSubject;
    private final HNewsApi api;
    private final DataPersister dataPersister;

    public DataRepository(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
        this.storiesSubject = BehaviorSubject.create();
        this.storySubject = BehaviorSubject.create();
        this.commentsSubject = BehaviorSubject.create();
        this.api = new HNewsApi();
    }

    public Observable<Vector<ContentValues>> getAllStories() {
        refreshStories();
        return storiesSubject;
    }

    private void refreshStories() {
        Observable.zip(
                api.getStories(Story.TYPE.top_story),
                api.getStories(Story.TYPE.new_story),
                api.getStories(Story.TYPE.best_story),
                api.getStories(Story.TYPE.show),
                api.getStories(Story.TYPE.ask),
                combineInBulk()
        ).doOnNext(new Action1<Vector<ContentValues>>() {
            @Override
            public void call(Vector<ContentValues> stories) {
                dataPersister.persistStories(stories);
            }
        }).subscribeOn(Schedulers.io()).subscribe(storiesSubject);
    }

    private static Func5<Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>> combineInBulk() {
        return new Func5<Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>>() {
            @Override
            public Vector<ContentValues> call(Vector<ContentValues> top, Vector<ContentValues> news, Vector<ContentValues> best, Vector<ContentValues> show, Vector<ContentValues> ask) {
                Vector<ContentValues> allStories = new Vector<>();
                allStories.addAll(top);
                allStories.addAll(news);
                allStories.addAll(best);
                allStories.addAll(show);
                allStories.addAll(ask);
                return allStories;
            }
        };
    }

    public Observable<Integer> getStoriesFrom(Story.TYPE type) {
        refreshStoryType(type);
        return storySubject;
    }

    private void refreshStoryType(Story.TYPE type) {
        api.getStories(type)
                .flatMap(new Func1<Vector<ContentValues>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final Vector<ContentValues> contentValueses) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                dataPersister.persistStories(contentValueses);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io()).subscribe(storySubject);
    }

    public Observable<Integer> getCommentsFromStory(Long storyId) {
        refreshComments(storyId);
        return storySubject;
    }

    private void refreshComments(final Long storyId) {
        api.getCommentsFromStory(storyId)
                .flatMap(new Func1<Vector<ContentValues>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final Vector<ContentValues> contentValueses) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                dataPersister.persistComments(contentValueses, storyId);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io()).subscribe(commentsSubject);
    }

}
