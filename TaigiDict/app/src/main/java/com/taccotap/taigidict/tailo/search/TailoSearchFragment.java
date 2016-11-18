package com.taccotap.taigidict.tailo.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taccotap.taigidict.R;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import io.realm.Realm;

import static android.content.ContentValues.TAG;
import static com.taccotap.taigidict.R.id.recyclerView;

public class TailoSearchFragment extends Fragment {

    @BindView(recyclerView)
    RecyclerView mRecyclerView;

    private TailoSearchAdapter mTailoSearchAdapter;
    private Realm mRealm;

    public TailoSearchFragment() {
        // Required empty public constructor
    }

    public static TailoSearchFragment newInstance() {
        TailoSearchFragment fragment = new TailoSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tailo_search, container, false);
        ButterKnife.bind(view);

        mRealm = Realm.getDefaultInstance();
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTailoSearchAdapter = new TailoSearchAdapter(getContext(), mRealm);
        mRecyclerView.setAdapter(mTailoSearchAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        mTailoSearchAdapter.getItemClickProcessor().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                onItemClick(integer);
            }
        });
    }

    private void onItemClick(int position) {
        final TlTaigiWord tlTaigiWord = mTailoSearchAdapter.getItem(position);

        // TODO: send to result screen with word id
        Log.d(TAG, "clicked tlTaigiWord = " + tlTaigiWord.lomaji);
    }
}
