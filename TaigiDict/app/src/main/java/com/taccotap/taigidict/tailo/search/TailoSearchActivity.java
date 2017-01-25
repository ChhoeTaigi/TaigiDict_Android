package com.taccotap.taigidict.tailo.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;

import com.taccotap.taigidict.R;
import com.taccotap.taigidict.tailo.word.TailoWordActivity;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import io.realm.Realm;

public class TailoSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String ACTION_SEARCH_LMJ = "ACTION_SEARCH_LMJ";
    public static final String ACTION_SEARCH_HOAGI = "ACTION_SEARCH_HOAGI";
    public static final String ACTION_SEARCH_ALL = "ACTION_SEARCH_ALL";

    private static final int SEARCH_TYPE_LOMAJI = 0;
    private static final int SEARCH_TYPE_HOAGI = 1;
    private static final int SEARCH_TYPE_ALL = 2;

    @BindView(R.id.searchView)
    SearchView mSearchView;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private TailoSearchAdapter mTailoSearchAdapter;
    private Realm mRealm;
    private int mCurrentSearchType = SEARCH_TYPE_LOMAJI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailo_search);
        ButterKnife.bind(this);

        handleIntent();

        mRealm = Realm.getDefaultInstance();
//        initActionBar();
        initRecyclerView();
        initSearch();
    }

//    private void initActionBar() {
//        final ActionBar supportActionBar = getSupportActionBar();
//        if (supportActionBar != null) {
//            supportActionBar.setDisplayShowHomeEnabled(true);
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
//        }
//    }

    private void handleIntent() {
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();

        if (ACTION_SEARCH_LMJ.equals(intent.getAction())) {
            mCurrentSearchType = 0;
        } else if (ACTION_SEARCH_HOAGI.equals(intent.getAction())) {
            mCurrentSearchType = 1;
        } else if (ACTION_SEARCH_ALL.equals(intent.getAction())) {
            mCurrentSearchType = 2;
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTailoSearchAdapter = new TailoSearchAdapter(this, mRealm);
        mRecyclerView.setAdapter(mTailoSearchAdapter);

        mTailoSearchAdapter.getItemClickProcessor().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                onItemClick(integer);
            }
        });
    }

    private void initSearch() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.requestFocus();

        if (mCurrentSearchType == SEARCH_TYPE_LOMAJI) {
            mSearchView.setQueryHint(getString(R.string.search_lomaji_hint));
        } else if (mCurrentSearchType == SEARCH_TYPE_HOAGI) {
            mSearchView.setQueryHint(getString(R.string.search_hoagi_hint));
        } else if (mCurrentSearchType == SEARCH_TYPE_ALL) {
            mSearchView.setQueryHint(getString(R.string.search_all_hint));
        }

        mSearchView.setOnQueryTextListener(this);
    }

    private void doSearch(String query) {
        query = query.trim();

        if (TextUtils.isEmpty(query)) {
            query = "Tâi-gí";
        }

        if (mCurrentSearchType == SEARCH_TYPE_LOMAJI) {
            mTailoSearchAdapter.searchLomaji(query);
        } else if (mCurrentSearchType == SEARCH_TYPE_HOAGI) {
            mTailoSearchAdapter.searchHoagi(query);
        } else if (mCurrentSearchType == SEARCH_TYPE_ALL) {
            mTailoSearchAdapter.searchAll(query);
        }

        mRecyclerView.scrollToPosition(0);
    }

    private void onItemClick(int position) {
        final TlTaigiWord tlTaigiWord = mTailoSearchAdapter.getItem(position);

        Intent intent = new Intent(this, TailoWordActivity.class);
        intent.setAction(TailoWordActivity.ACTION_TAILO_WORD_MAIN_CODE);
        intent.putExtra(TailoWordActivity.EXTRA_TAILO_WORD_MAIN_CODE, tlTaigiWord.getMainCode());
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        doSearch(newText);
        return true;
    }
}
