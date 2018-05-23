package com.chronaxia.lowpolyworld.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.adapter.CustomViewPagerAdapter;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.chronaxia.lowpolyworld.presenter.ScenicSpotsPresenter;
import com.chronaxia.lowpolyworld.presenter.contract.ScenicSpotsContract;
import com.chronaxia.lowpolyworld.util.ValueUnit;
import com.chronaxia.lowpolyworld.view.custom.MyExpandingPagerFactory;
import com.chronaxia.lowpolyworld.view.fragment.CustomExpandingFragment;
import com.chronaxia.lowpolyworld.view.fragment.CustomFragmentBottom;
import com.chronaxia.lowpolyworld.view.fragment.CustomFragmentTop;
import com.jakewharton.rxbinding2.view.RxView;
import com.qslll.library.ExpandingPagerFactory;
import com.qslll.library.fragments.ExpandingFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ScenicSpotsActivity extends BaseActivity implements ScenicSpotsContract.View, ExpandingFragment.OnExpandingClickListener,
        CustomFragmentTop.OnFragmentInteractionListener, CustomFragmentBottom.OnFragmentInteractionListener{

    @BindView(R.id.iv_scenic_spot_back)
    ImageView ivScenicSpotBack;
    @BindView(R.id.vp_scenic_spots)
    ViewPager vpScenicSpots;

    private String continent;
    private ScenicSpotsContract.Presenter presenter;
    private CustomViewPagerAdapter customViewPagerAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_scenic_spots;
    }

    @Override
    protected void initData() {
        continent = getIntent().getStringExtra("continent");
        presenter = new ScenicSpotsPresenter(this, this, getResources().getIdentifier(ValueUnit.turnToXmlName(continent), "xml", getPackageName()));
    }

    @Override
    protected void initView() {
        customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        vpScenicSpots.setAdapter(customViewPagerAdapter);
        MyExpandingPagerFactory.setupViewPager(vpScenicSpots);
        vpScenicSpots.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ExpandingFragment expandingFragment = ExpandingPagerFactory.getCurrentFragment(vpScenicSpots);
                if(expandingFragment != null && expandingFragment.isOpenend()){
                    expandingFragment.close();
                }
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (continent != null && !"".equals(continent)) {
            presenter.loadScenicSpots();
        }
        RxView.clicks(ivScenicSpotBack)
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
    }

    @Override
    public void updateScenicSpots(List<ScenicSpot> scenicSpotList) {
        customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.updateScenicSpots(scenicSpotList);
        vpScenicSpots.setAdapter(customViewPagerAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onLowpoly() {
    }

    private void startInfoActivity(View view, ScenicSpot scenicSpot) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, getString(R.string.transition_name));
        Intent intent = new Intent();
        if (scenicSpot.getLocation().equals("zoo")) {
        } else {
            intent = IntroduceActivity.newInstance(this, scenicSpot);
        }
        ActivityCompat.startActivity(this, intent,options.toBundle());
    }

    @Override
    public void onExpandingClick(View v) {
        View view = v.findViewById(R.id.iv_scenic_picture);
        startInfoActivity(view,customViewPagerAdapter.getList().get(vpScenicSpots.getCurrentItem()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }

}
