package com.chronaxia.lowpolyworld.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.model.entity.Question;
import com.chronaxia.lowpolyworld.presenter.DistinguishPresenter;
import com.chronaxia.lowpolyworld.presenter.contract.DistinguishContract;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class DistinguishActivity extends BaseActivity implements DistinguishContract.View{

    @BindView(R.id.iv_question)
    ImageView ivQuestion;
    @BindView(R.id.iv_left_answer_picture)
    ImageView ivLeftAnswerPicture;
    @BindView(R.id.tv_left_answer_name)
    TextView tvLeftAnswerName;
    @BindView(R.id.iv_right_answer_picture)
    ImageView ivRightAnswerPicture;
    @BindView(R.id.tv_right_answer_name)
    TextView tvRightAnswerName;
    @BindView(R.id.iv_distinguish_back)
    ImageView ivDistinguishBack;
    @BindView(R.id.iv_distinguish_forward)
    ImageView ivDistinguishForward;
    @BindView(R.id.tv_question_name)
    TextView tvQuestionName;
    @BindView(R.id.tv_question_english)
    TextView tvQuestionEnglish;
    @BindView(R.id.iv_left_check)
    ImageView ivLeftCheck;
    @BindView(R.id.iv_right_check)
    ImageView ivRightCheck;

    private DistinguishContract.Presenter presenter;
    private List<Question> questionList;
    private Question selectQuestion;
    private boolean lock = false;

    @Override
    protected int setContentView() {
        return R.layout.activity_distinguish;
    }

    @Override
    protected void initData() {
        presenter = new DistinguishPresenter(this, this);
        presenter.loadQuestions(getResources().getXml(R.xml.question));
    }

    @Override
    protected void initView() {
        RxView.clicks(ivDistinguishBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        finish();
                    }
                });
        RxView.clicks(ivDistinguishForward)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Question question = getRandomQuestion();
                        if (question != null) {
                            initQuestion(question);
                        }
                    }
                });
        RxView.clicks(tvLeftAnswerName)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        leftAnswerSelect();
                    }
                });
        RxView.clicks(ivLeftAnswerPicture)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        leftAnswerSelect();
                    }
                });
        RxView.clicks(tvRightAnswerName)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        rightAnswerSelect();
                    }
                });
        RxView.clicks(ivRightAnswerPicture)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        rightAnswerSelect();
                    }
                });
    }

    @Override
    public void updateQuestions(List<Question> questionList) {
        this.questionList = questionList;
        Question question = getRandomQuestion();
        if (question != null) {
            initQuestion(question);
        }
    }

    private void initQuestion(Question question) {
        if (question != null) {
            int leftId = getResources().getIdentifier(question.getLeftAnswerPicture(), "mipmap", getPackageName());
            int rightId = getResources().getIdentifier(question.getRightAnswerPicture(), "mipmap", getPackageName());
            int questionId = getResources().getIdentifier(question.getQuestionPicture(), "mipmap", getPackageName());
            Glide.with(this)
                    .load(questionId)
                    .into(ivQuestion);
            Glide.with(this)
                    .load(leftId)
                    .into(ivLeftAnswerPicture);
            Glide.with(this)
                    .load(rightId)
                    .into(ivRightAnswerPicture);
            tvLeftAnswerName.setText(question.getLeftAnswerName());
            tvRightAnswerName.setText(question.getRightAnswerName());
            tvQuestionName.setText("");
            tvQuestionEnglish.setText("");
            ivLeftCheck.setImageDrawable(null);
            ivRightCheck.setImageDrawable(null);
            selectQuestion = question;
            lock = false;
        }
    }

    private Question getRandomQuestion() {
        if (questionList.size() != 0) {
            Random random = new Random();
            int n = random.nextInt(questionList.size());
            return questionList.get(n);
        }
        return null;
    }

    private void leftAnswerSelect() {
        if (lock) return;
        if ("left".equals(selectQuestion.getAnswer())) {
            ivLeftCheck.setImageResource(R.drawable.ic_true);
            ivRightCheck.setImageResource(R.drawable.ic_false);
            answerTrue();
        } else {
            answerFalse();
        }
    }

    private void rightAnswerSelect() {
        if (lock) return;
        if ("right".equals(selectQuestion.getAnswer())) {
            ivRightCheck.setImageResource(R.drawable.ic_true);
            ivLeftCheck.setImageResource(R.drawable.ic_false);
            answerTrue();
        } else {
            answerFalse();
        }
    }

    private void answerTrue() {
        lock = true;
        tvQuestionName.setText(selectQuestion.getQuestionName());
        tvQuestionEnglish.setText(selectQuestion.getQuestionEnglish());
        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();
        spring.setCurrentValue(0.7f);
        spring.setSpringConfig(new SpringConfig(50,5));
        spring.addListener(new SimpleSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                float currentValue = (float) spring.getCurrentValue();
                ivQuestion.setScaleX(currentValue);
                ivQuestion.setScaleY(currentValue);
            }
        });
        spring.setEndValue(1);
    }

    private void answerFalse() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
