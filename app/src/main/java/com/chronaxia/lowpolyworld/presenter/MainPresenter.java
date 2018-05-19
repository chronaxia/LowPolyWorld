package com.chronaxia.lowpolyworld.presenter;

import android.util.Log;

import com.chronaxia.lowpolyworld.model.ContinentModel;
import com.chronaxia.lowpolyworld.model.entity.Continent;
import com.chronaxia.lowpolyworld.presenter.contract.MainContract;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 一非 on 2018/5/19.
 */

public class MainPresenter extends BaseActivityPresenter implements MainContract.Presenter{
    private MainContract.View view;

    public MainPresenter(LifecycleProvider<ActivityEvent> provider, MainContract.View view) {
        super(provider);
        this.view = view;
    }

    @Override
    public void loadContinent(final String name) {
        Observable.create(new ObservableOnSubscribe<Continent>() {
            @Override
            public void subscribe(ObservableEmitter<Continent> e) throws Exception {
                e.onNext(ContinentModel.getInstance().getContinent(name));
            }
        })
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<Continent>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Continent>() {
                    @Override
                    public void accept(Continent continent) throws Exception {
                        if (view != null) {
                            view.showContinent(continent);
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
