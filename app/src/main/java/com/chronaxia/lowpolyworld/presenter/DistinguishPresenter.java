package com.chronaxia.lowpolyworld.presenter;

import android.util.Log;

import com.chronaxia.lowpolyworld.model.QuestionModel;
import com.chronaxia.lowpolyworld.model.entity.Question;
import com.chronaxia.lowpolyworld.presenter.contract.DistinguishContract;
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
 * Created by 一非 on 2018/5/23.
 */

public class DistinguishPresenter extends BaseActivityPresenter implements DistinguishContract.Presenter {

    private DistinguishContract.View view;
    private QuestionModel model;

    public DistinguishPresenter(LifecycleProvider<ActivityEvent> provider, DistinguishContract.View view) {
        super(provider);
        this.view = view;
        model = new QuestionModel();
    }

    @Override
    public void loadQuestions(final XmlPullParser parser) {
        Observable.create(new ObservableOnSubscribe<List<Question>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Question>> e) throws Exception {
                e.onNext(model.loadQuestions(parser));
            }
        })
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<List<Question>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Question>>() {
                    @Override
                    public void accept(List<Question> questionList) throws Exception {
                        if (view != null) {
                            view.updateQuestions(questionList);
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
