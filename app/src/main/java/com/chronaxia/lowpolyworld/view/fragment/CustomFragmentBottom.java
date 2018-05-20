package com.chronaxia.lowpolyworld.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;
import com.jakewharton.rxbinding2.view.RxView;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class CustomFragmentBottom extends BaseFragment {

    @BindView(R.id.tv_scenic_spot_phonetic)
    TextView tvScenicSpotPhonetic;
    @BindView(R.id.tv_scenic_spot_name)
    TextView tvScenicSpotName;

    private OnFragmentInteractionListener mListener;
    private JumpingBeans jumpingBeans;
    private ScenicSpot scenicSpot;

    public CustomFragmentBottom() {
        // Required empty public constructor
    }

    public static CustomFragmentBottom newInstance(ScenicSpot scenicSpot) {
        CustomFragmentBottom fragment = new CustomFragmentBottom();
        Bundle args = new Bundle();
        args.putSerializable("scenic_spot", scenicSpot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_custom_fragment_bottom;
    }

    @Override
    protected void initData() {
        scenicSpot = (ScenicSpot) getArguments().getSerializable("scenic_spot");
    }

    @Override
    protected void initView() {
        if (scenicSpot != null) {
            tvScenicSpotPhonetic.setText(scenicSpot.getPhonetic());
            jumpingBeans = JumpingBeans.with(tvScenicSpotPhonetic)
                    .makeTextJump(0, tvScenicSpotPhonetic.getText().toString().length())
                    .setIsWave(true)
                    .setLoopDuration(2000)
                    .build();
            tvScenicSpotName.setText(scenicSpot.getName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        jumpingBeans.stopJumping();
    }

    public interface OnFragmentInteractionListener {
        void onLowpoly();
    }
}
