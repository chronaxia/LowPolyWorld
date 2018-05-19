package com.chronaxia.lowpolyworld.presenter.contract;

import android.app.Activity;

import com.chronaxia.lowpolyworld.presenter.IBasePresenter;
import com.chronaxia.lowpolyworld.presenter.IBaseView;

import java.util.List;

/**
 * Created by 一非 on 2018/5/17.
 */

public interface StartContract {
    interface View extends IBaseView {
        void getPermissionsSuccess();
        void getPermissionsFailed();
    }

    interface Presenter extends IBasePresenter {
        void getPermissions(Activity activity);
    }
}
