package com.taccotap.taigidict.tailo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.taccotap.taigidict.R;
import com.taccotap.taigidict.tailo.search.TailoSearchAdapter;
import com.taccotap.taigidict.ui.ClearableEditText;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TlSearchActivity extends AppCompatActivity {

    @BindView(R.id.clearableEditText)
    ClearableEditText mClearableEditText;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private TailoSearchAdapter mTailoSearchAdapter;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tl_search);
        ButterKnife.bind(this);

        mRealm = Realm.getDefaultInstance();

        initRecyclerView();
        initSearchEditText();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTailoSearchAdapter = new TailoSearchAdapter(this, mRealm);
        mRecyclerView.setAdapter(mTailoSearchAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
    }

    private void initSearchEditText() {
        mClearableEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTailoSearchAdapter.search(editable.toString());
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mRealm != null) {
            mRealm.close();
        }
    }
}
