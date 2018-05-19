package com.chronaxia.lowpolyworld.view.activity;

import android.content.Intent;
import android.widget.Toast;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.chronaxia.lowpolyworld.presenter.StartPresenter;
import com.chronaxia.lowpolyworld.presenter.contract.StartContract;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class StartActivity extends BaseActivity implements StartContract.View{

    private StartContract.Presenter presenter;

    @Override
    protected int setContentView() {
        return R.layout.activity_start;
    }

    @Override
    protected void initData() {
        presenter = new StartPresenter(this, this);
        presenter.getPermissions(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void getPermissionsSuccess() {
        LowPolyWorldApp.getInstance().initDbHelp();
        LowPolyWorldApp.getInstance().initWindow();
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .compose(this.<Integer>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
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
