package com.chronaxia.lowpolyworld.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.chronaxia.lowpolyworld.view.fragment.CustomExpandingFragment;
import com.qslll.library.ExpandingViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一非 on 2018/5/19.
 */

public class CustomViewPagerAdapter  extends ExpandingViewPagerAdapter {

    private List<ScenicSpot> list;

    public CustomViewPagerAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
    }

    public void updateScenicSpots(List<ScenicSpot> scenicSpotList) {
        list = scenicSpotList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return CustomExpandingFragment.newInstance(list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
