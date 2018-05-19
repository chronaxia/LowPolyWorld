package com.chronaxia.lowpolyworld.view.activity;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.adapter.CustomViewPagerAdapter;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.chronaxia.lowpolyworld.presenter.ScenicSpotsPresenter;
import com.chronaxia.lowpolyworld.presenter.contract.ScenicSpotsContract;
import com.chronaxia.lowpolyworld.util.ValueUnit;
import com.chronaxia.lowpolyworld.view.custom.MyExpandingPagerFactory;
import com.chronaxia.lowpolyworld.view.fragment.CustomFragmentBottom;
import com.chronaxia.lowpolyworld.view.fragment.CustomFragmentTop;
import com.qslll.library.ExpandingPagerFactory;
import com.qslll.library.fragments.ExpandingFragment;

import java.util.List;

import butterknife.BindView;

public class ScenicSpotsActivity extends BaseActivity implements ScenicSpotsContract.View, ExpandingFragment.OnExpandingClickListener,
        CustomFragmentTop.OnFragmentInteractionListener, CustomFragmentBottom.OnFragmentInteractionListener{

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
    }

    @Override
    public void updateScenicSpots(List<ScenicSpot> scenicSpotList) {
        customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.updateScenicSpots(scenicSpotList);
        vpScenicSpots.setAdapter(customViewPagerAdapter);
    }

    @Override
    public void onExpandingClick(View view) {
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }

}
