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
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<Permission>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            startView.getPermissionsSuccess();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            startView.getPermissionsFailed();
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
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
