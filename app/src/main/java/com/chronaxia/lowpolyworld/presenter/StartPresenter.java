package com.chronaxia.lowpolyworld.presenter;

import android.Manifest;
import android.app.Activity;

import com.chronaxia.lowpolyworld.presenter.contract.StartContract;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by 一非 on 2018/4/23.
 */

public class StartPresenter extends BaseActivityPresenter implements StartContract.Presenter{

    private StartContract.View view;

    public StartPresenter(LifecycleProvider<ActivityEvent> provider, StartContract.View view) {
        super(provider);
        this.view = view;
    }

    @Override
    public void getPermissions(Activity activity) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<Boolean>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            if (view != null) {
                                view.getPermissionsSuccess();
                            }
                        } else {
                            if (view != null) {
                                view.getPermissionsFailed();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.getPermissionsFailed();
                        }
                    }
                });
    }

    @Override
    public void doDestroy() {
        view = null;
    }
}
