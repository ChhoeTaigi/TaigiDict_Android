package com.taccotap.taigidict.tailo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.taccotap.taigidict.R;
import com.taccotap.taigidict.tailo.search.TailoSearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TailoDictFragment extends Fragment {

    @BindView(R.id.button_search_lmj)
    Button mSearchLmjButton;

    @BindView(R.id.button_search_hoagi)
    Button mSearchHoagiButton;

    @BindView(R.id.button_search_all)
    Button mSearchAllButton;

    public TailoDictFragment() {
        // Required empty public constructor
    }

    public static TailoDictFragment newInstance() {
        return new TailoDictFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tailo_dict, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.button_search_lmj)
    public void onClickSearchLmjButton(Button button) {
        Intent intent = new Intent(getContext(), TailoSearchActivity.class);
        intent.setAction(TailoSearchActivity.ACTION_SEARCH_LMJ);
        getContext().startActivity(intent);
    }


    @OnClick(R.id.button_search_hoagi)
    public void onClickSearchHoagiButton(Button button) {
        Intent intent = new Intent(getContext(), TailoSearchActivity.class);
        intent.setAction(TailoSearchActivity.ACTION_SEARCH_HOAGI);
        getContext().startActivity(intent);
    }


    @OnClick(R.id.button_search_all)
    public void onClickSearchAllButton(Button button) {
        Intent intent = new Intent(getContext(), TailoSearchActivity.class);
        intent.setAction(TailoSearchActivity.ACTION_SEARCH_ALL);
        getContext().startActivity(intent);
    }
}
