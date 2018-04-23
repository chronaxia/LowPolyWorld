package com.chronaxia.lowpolyworld.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.chronaxia.lowpolyworld.presenter.IStartPresenter;
import com.chronaxia.lowpolyworld.presenter.StartPresenterImpl;
import com.chronaxia.lowpolyworld.view.viewer.IStartView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class StartActivity extends BaseActivity implements IStartView{

    private IStartPresenter startPresenter;

    @Override
    protected int setContentView() {
        return R.layout.activity_start;
    }

    @Override
    protected void initData() {
        startPresenter = new StartPresenterImpl(this, this);
        startPresenter.getPermissions(this);
    }

    @Override
    protected void initView() {
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .compose(this.<Integer>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void getPermissionsSuccess() {
        LowPolyWorldApp.getInstance().initDbHelp();
    }

    @Override
    public void getPermissionsFailed() {
        Toasty.error(this, "拒绝权限将无法正常运行", Toast.LENGTH_LONG, true).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
