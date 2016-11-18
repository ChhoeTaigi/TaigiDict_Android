package com.taccotap.taigidict.tailo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taccotap.taigidict.R;

import butterknife.ButterKnife;

public class TailoDictFragment extends Fragment {

    public TailoDictFragment() {
        // Required empty public constructor
    }

    public static TailoDictFragment newInstance() {
        TailoDictFragment fragment = new TailoDictFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tailo_dict, container, false);
        ButterKnife.bind(view);

        return view;
    }

}
