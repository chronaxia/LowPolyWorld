package com.chronaxia.lowpolyworld.presenter;

import android.Manifest;
import android.app.Activity;

import com.chronaxia.lowpolyworld.view.viewer.IStartView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by 一非 on 2018/4/23.
 */

public class StartPresenterImpl extends BaseActivityPresenter implements IStartPresenter{

    private IStartView startView;

    public StartPresenterImpl(LifecycleProvider<ActivityEvent> provider, IStartView startView) {
        super(provider);
        this.startView = startView;
    }

    @Override
    public void getPermissions(Activity activity) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<Boolean>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            if (startView != null) {
                                startView.getPermissionsSuccess();
                            }
                        } else {
                            if (startView != null) {
                                startView.getPermissionsFailed();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (startView != null) {
                            startView.getPermissionsFailed();
                        }
                    }
                });
    }

    @Override
    public void doDestroy() {
        startView = null;
    }
}
