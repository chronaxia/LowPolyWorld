package com.chronaxia.lowpolyworld.presenter.contract;

import android.app.Activity;

import com.chronaxia.lowpolyworld.model.entity.Continent;
import com.chronaxia.lowpolyworld.presenter.IBasePresenter;
import com.chronaxia.lowpolyworld.presenter.IBaseView;

/**
 * Created by 一非 on 2018/5/19.
 */

public interface MainContract {
    interface View extends IBaseView {
        void showContinent(Continent continent);
    }

    interface Presenter extends IBasePresenter {
        void loadContinent(String name);
    }
}
