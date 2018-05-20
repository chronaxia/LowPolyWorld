package com.chronaxia.lowpolyworld.view.custom;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.qslll.library.ExpandingPagerFactory;
import com.qslll.library.ExpandingViewPagerTransformer;

/**
 * Created by 一非 on 2018/5/19.
 */

public class MyExpandingPagerFactory extends ExpandingPagerFactory {

    public static void setupViewPager(ViewPager viewPager) {
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
//        layoutParams.height = ((Activity)viewPager.getContext()).getWindowManager().getDefaultDisplay().getHeight() / 7 * 5;
//        layoutParams.width = (int)((double)layoutParams.height / 0.75D);
        layoutParams.height = (int)(LowPolyWorldApp.getInstance().getY() * 0.7);
        layoutParams.width = (int)(LowPolyWorldApp.getInstance().getX() * 0.7);
        viewPager.setOffscreenPageLimit(2);
        if(viewPager.getParent() instanceof ViewGroup) {
            ViewGroup viewParent = (ViewGroup)viewPager.getParent();
            viewParent.setClipChildren(false);
            viewPager.setClipChildren(false);
        }

        viewPager.setPageTransformer(true, new ExpandingViewPagerTransformer());
    }
}
