package com.chronaxia.lowpolyworld.presenter.contract;

import com.chronaxia.lowpolyworld.model.entity.Question;
import com.chronaxia.lowpolyworld.presenter.IBasePresenter;
import com.chronaxia.lowpolyworld.presenter.IBaseView;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

/**
 * Created by 一非 on 2018/5/23.
 */

public interface DistinguishContract {
    interface View extends IBaseView {
        void updateQuestions(List<Question> questionList);
    }

    interface Presenter extends IBasePresenter {
        void loadQuestions(XmlPullParser parser);
    }
}
