package com.chronaxia.lowpolyworld.presenter;

import android.util.Log;

import com.chronaxia.lowpolyworld.model.TemplateModel;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.chronaxia.lowpolyworld.presenter.contract.PaintContract;
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
 * Created by 一非 on 2018/5/24.
 */

public class PaintPresenter extends BaseActivityPresenter implements PaintContract.Presenter {

    private PaintContract.View view;
    private TemplateModel model;

    public PaintPresenter(LifecycleProvider<ActivityEvent> provider, PaintContract.View view) {
        super(provider);
        this.view = view;
        model = new TemplateModel();
    }

    @Override
    public void loadTemplate(final XmlPullParser parser) {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                e.onNext(model.loadTemplates(parser));
            }
        })
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<List<String>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> stringList) throws Exception {
                        if (view != null) {
                            view.updateTemplate(stringList);
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
