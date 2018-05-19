package com.chronaxia.lowpolyworld.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;

import butterknife.BindView;

public class CustomFragmentTop extends BaseFragment {

    @BindView(R.id.tv_test_top)
    TextView tvTestTop;

    private OnFragmentInteractionListener mListener;

    private ScenicSpot scenicSpot;

    public CustomFragmentTop() {
        // Required empty public constructor
    }

    public static CustomFragmentTop newInstance(ScenicSpot scenicSpot) {
        CustomFragmentTop fragment = new CustomFragmentTop();
        Bundle args = new Bundle();
        args.putSerializable("scenic_spot", scenicSpot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_custom_fragment_top;
    }

    @Override
    protected void initData() {
        scenicSpot = (ScenicSpot) getArguments().getSerializable("scenic_spot");
    }

    @Override
    protected void initView() {
        if (scenicSpot != null) {
            tvTestTop.setText(scenicSpot.getName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
