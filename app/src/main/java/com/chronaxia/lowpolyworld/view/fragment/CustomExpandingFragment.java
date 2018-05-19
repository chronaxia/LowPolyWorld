package com.chronaxia.lowpolyworld.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.qslll.library.fragments.ExpandingFragment;

/**
 * Created by 一非 on 2018/5/19.
 */

public class CustomExpandingFragment extends ExpandingFragment {

    private ScenicSpot scenicSpot;

    public static CustomExpandingFragment newInstance(ScenicSpot scenicSpot){
        CustomExpandingFragment fragment = new CustomExpandingFragment();
        Bundle args = new Bundle();
        args.putSerializable("scenic_spot", scenicSpot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            scenicSpot = (ScenicSpot) args.getSerializable("scenic_spot");
        }
    }

    @Override
    public Fragment getFragmentTop() {
        return CustomFragmentTop.newInstance(scenicSpot);
    }

    @Override
    public Fragment getFragmentBottom() {
        return CustomFragmentBottom.newInstance(scenicSpot);
    }
}
