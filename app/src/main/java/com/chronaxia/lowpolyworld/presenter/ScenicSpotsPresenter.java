package com.chronaxia.lowpolyworld.presenter;

import com.chronaxia.lowpolyworld.model.ScenicSpotsModel;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.chronaxia.lowpolyworld.presenter.contract.ScenicSpotsContract;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 一非 on 2018/5/19.
 */

public class ScenicSpotsPresenter extends BaseActivityPresenter implements ScenicSpotsContract.Presenter {

    private ScenicSpotsContract.View view;

    public ScenicSpotsPresenter(LifecycleProvider<ActivityEvent> provider, ScenicSpotsContract.View view, final int id) {
        super(provider);
        this.view = view;
        ScenicSpotsModel.getInstance().initData(id);
    }

    @Override
    public void loadScenicSpots() {
        if (view != null) {
            view.updateScenicSpots(ScenicSpotsModel.getInstance().getScenicSpotList());
        }
    }

    @Override
    public void doDestroy() {
        view = null;
    }
}
