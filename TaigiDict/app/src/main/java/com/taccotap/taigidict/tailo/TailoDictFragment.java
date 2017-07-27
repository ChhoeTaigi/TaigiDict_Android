package com.taccotap.taigidict.tailo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.taccotap.taigidict.R;
import com.taccotap.taigidict.tailo.search.TailoSearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TailoDictFragment extends Fragment {

    @BindView(R.id.descIcon1)
    ImageView mDescIcon1ImageView;

    @BindView(R.id.descIcon2)
    ImageView mDescIcon2ImageView;

    @BindView(R.id.descIcon3)
    ImageView mDescIcon3ImageView;

    @BindView(R.id.descText3)
    TextView mDesc3TextView;

    @BindView(R.id.button_search_lmj)
    Button mSearchLmjButton;

    @BindView(R.id.button_search_hanji)
    Button mSearchHanjiButton;

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

        final IconicsDrawable infoIconDrawable = new IconicsDrawable(getContext()).icon(CommunityMaterial.Icon.cmd_information_outline).color(getContext().getResources().getColor(R.color.colorPrimary)).sizeDp(18);
        mDescIcon1ImageView.setImageDrawable(infoIconDrawable);
        mDescIcon2ImageView.setImageDrawable(infoIconDrawable);
        mDescIcon3ImageView.setImageDrawable(infoIconDrawable);

        mDesc3TextView.setClickable(true);
        mDesc3TextView.setMovementMethod(LinkMovementMethod.getInstance());
        mDesc3TextView.setText(Html.fromHtml(getString(R.string.fragment_tailo_search_description3)));

        return view;
    }

    @OnClick(R.id.button_search_lmj)
    public void onClickSearchLmjButton(Button button) {
        Intent intent = new Intent(getContext(), TailoSearchActivity.class);
        intent.setAction(TailoSearchActivity.ACTION_SEARCH_LMJ);
        getContext().startActivity(intent);
    }

    @OnClick(R.id.button_search_hanji)
    public void onClickSearchHanjiButton(Button button) {
        Intent intent = new Intent(getContext(), TailoSearchActivity.class);
        intent.setAction(TailoSearchActivity.ACTION_SEARCH_HANJI);
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
