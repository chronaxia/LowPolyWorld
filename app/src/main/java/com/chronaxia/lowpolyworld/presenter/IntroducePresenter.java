package com.chronaxia.lowpolyworld.presenter;

import android.util.Log;

import com.chronaxia.lowpolyworld.model.IntroduceModel;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.chronaxia.lowpolyworld.presenter.contract.IntroduceContract;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 一非 on 2018/5/21.
 */

public class IntroducePresenter extends BaseActivityPresenter implements IntroduceContract.Presenter{

    private IntroduceContract.View view;
    private IntroduceModel model;

    public IntroducePresenter(LifecycleProvider<ActivityEvent> provider, IntroduceContract.View view) {
        super(provider);
        this.view = view;
        model = new IntroduceModel();
    }

    @Override
    public void loadScenicSpots(final XmlPullParser parser) {
        Observable.create(new ObservableOnSubscribe<List<ScenicSpot>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ScenicSpot>> e) throws Exception {
                e.onNext(model.loadIntroduceScenicSpots(parser));
            }
        })
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<List<ScenicSpot>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ScenicSpot>>() {
                    @Override
                    public void accept(List<ScenicSpot> scenicSpotList) throws Exception {
                        if (view != null) {
                            view.updateScenicSpots(scenicSpotList);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("asd", throwable.getMessage());
                    }
                });
    }

    @Override
    public void doDestroy() {
        view = null;
    }
}
