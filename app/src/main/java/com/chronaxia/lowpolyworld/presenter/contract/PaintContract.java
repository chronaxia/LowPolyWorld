package com.chronaxia.lowpolyworld.presenter.contract;

import com.chronaxia.lowpolyworld.presenter.IBasePresenter;
import com.chronaxia.lowpolyworld.presenter.IBaseView;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

/**
 * Created by 一非 on 2018/5/24.
 */

public interface PaintContract {
    interface View extends IBaseView {
        void updateTemplate(List<String> templateList);
    }

    interface Presenter extends IBasePresenter {
        void loadTemplate(XmlPullParser parser);
    }
}
