package com.chronaxia.lowpolyworld.presenter.contract;

import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.chronaxia.lowpolyworld.presenter.IBasePresenter;
import com.chronaxia.lowpolyworld.presenter.IBaseView;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

/**
 * Created by 一非 on 2018/5/21.
 */

public interface IntroduceContract {
    interface View extends IBaseView {
        void updateScenicSpots(List<ScenicSpot> scenicSpotList);
    }

    interface Presenter extends IBasePresenter {
        void loadScenicSpots(XmlPullParser parser);
    }
}
