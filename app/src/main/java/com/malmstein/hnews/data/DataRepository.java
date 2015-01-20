package com.malmstein.hnews.data;

import android.content.ContentValues;

import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.model.Story;

import java.util.Vector;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func5;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class DataRepository {

    private final BehaviorSubject<Vector<ContentValues>> storiesSubject;
    private final HNewsApi api;
    private final DatabasePersister databasePersister;

    public DataRepository(DatabasePersister databasePersister) {
        this.databasePersister = databasePersister;
        this.storiesSubject = BehaviorSubject.create();
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
                databasePersister.persistStories(stories);
            }
        }).subscribeOn(Schedulers.io()).subscribe(storiesSubject);
    }

    private static Func5<Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>> combineInBulk() {
        return new Func5<Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>, Vector<ContentValues>>() {
            @Override
            public Vector<ContentValues> call(Vector<ContentValues> top, Vector<ContentValues> news, Vector<ContentValues> best, Vector<ContentValues> show, Vector<ContentValues> ask) {
                Vector<ContentValues> allStories = null;
                allStories.addAll(top);
                allStories.addAll(news);
                allStories.addAll(best);
                allStories.addAll(show);
                allStories.addAll(ask);
                return allStories;
            }
        };
    }

}
