package com.taccotap.taigidict.tailo.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.taccotap.taigidict.BuildConfig;
import com.taccotap.taigidict.R;
import com.taccotap.taigidict.converter.LomajiUnicodeConverter;
import com.taccotap.taigidict.converter.PojToTailoConverter;
import com.taccotap.taigidict.tailo.utils.TailoConstants;
import com.taccotap.taigidict.tailo.word.TailoWordActivity;
import com.taccotap.taigidict.utils.LomajiSearchUtils;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TailoSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = TailoSearchActivity.class.getSimpleName();

    public static final String ACTION_SEARCH_LMJ = "ACTION_SEARCH_LMJ";
    public static final String ACTION_SEARCH_HANJI = "ACTION_SEARCH_HANJI";
    public static final String ACTION_SEARCH_HOAGI = "ACTION_SEARCH_HOAGI";
    public static final String ACTION_SEARCH_ALL = "ACTION_SEARCH_ALL";

    private static final int SEARCH_TYPE_LOMAJI = 0;
    private static final int SEARCH_TYPE_HANJI = 1;
    private static final int SEARCH_TYPE_HOAGI = 2;
    private static final int SEARCH_TYPE_ALL = 3;

    @BindView(R.id.searchView)
    SearchView mSearchView;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.searchRangeRadioGroup)
    RadioGroup mSearchRangeRadioGroup;

    @BindView(R.id.searchContainsRadioButton)
    RadioButton mSearchContainsRadioButton;

    @BindView(R.id.searchEqualRadioButton)
    RadioButton mSearchEqualRadioButton;

    private TailoSearchAdapter mTailoSearchAdapter;
    private Realm mRealm;
    private int mCurrentSearchType = SEARCH_TYPE_LOMAJI;

    private String mCurrentQueryString = TailoConstants.DEFAULT_QUERY_STRING;
    private boolean mIsCurrentSearchEquals = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailo_search);
        ButterKnife.bind(this);

        handleIntent();

        mRealm = Realm.getDefaultInstance();

        initRecyclerView();
        initRadioButton();
        initSearch();
    }

    private void handleIntent() {
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();

        if (ACTION_SEARCH_LMJ.equals(intent.getAction())) {
            mCurrentSearchType = SEARCH_TYPE_LOMAJI;
            setTitle(getString(R.string.nav_dict_tailo) + " (" + getString(R.string.fragment_tailo_search_button_lomaji) + ")");
        } else if (ACTION_SEARCH_HANJI.equals(intent.getAction())) {
            mCurrentSearchType = SEARCH_TYPE_HANJI;
            setTitle(getString(R.string.nav_dict_tailo) + " (" + getString(R.string.fragment_tailo_search_button_hanji) + ")");
        } else if (ACTION_SEARCH_HOAGI.equals(intent.getAction())) {
            mCurrentSearchType = SEARCH_TYPE_HOAGI;
            setTitle(getString(R.string.nav_dict_tailo) + " (" + getString(R.string.fragment_tailo_search_button_hoagi) + ")");
        } else if (ACTION_SEARCH_ALL.equals(intent.getAction())) {
            mCurrentSearchType = SEARCH_TYPE_ALL;
            setTitle(getString(R.string.nav_dict_tailo) + " (" + getString(R.string.fragment_tailo_search_button_all) + ")");
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

    private void initRadioButton() {
        mSearchContainsRadioButton.setOnCheckedChangeListener(this);
        mSearchEqualRadioButton.setOnCheckedChangeListener(this);
    }

    private void initSearch() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.requestFocus();

        if (mCurrentSearchType == SEARCH_TYPE_LOMAJI) {
            mSearchView.setQueryHint(getString(R.string.search_lomaji_hint));
            mSearchRangeRadioGroup.setVisibility(View.VISIBLE);
        } else if (mCurrentSearchType == SEARCH_TYPE_HANJI) {
            mSearchView.setQueryHint(getString(R.string.search_hanji_hint));
            mSearchRangeRadioGroup.setVisibility(View.VISIBLE);
        } else if (mCurrentSearchType == SEARCH_TYPE_HOAGI) {
            mSearchView.setQueryHint(getString(R.string.search_hoagi_hint));
            mSearchRangeRadioGroup.setVisibility(View.VISIBLE);
        } else if (mCurrentSearchType == SEARCH_TYPE_ALL) {
            mSearchView.setQueryHint(getString(R.string.search_all_hint));
            mSearchRangeRadioGroup.setVisibility(View.GONE);
        }

        mSearchView.setOnQueryTextListener(this);
    }

    private void doSearch() {
        doSearch(mCurrentQueryString, mIsCurrentSearchEquals);
    }

    private void doSearch(String query) {
        doSearch(query, mIsCurrentSearchEquals);
    }

    private void doSearch(String query, boolean isSearchEquals) {
        mCurrentQueryString = query;

        if (TextUtils.isEmpty(query)) {
            query = TailoConstants.DEFAULT_QUERY_STRING;
        }

        if (mCurrentSearchType == SEARCH_TYPE_LOMAJI) {
            mTailoSearchAdapter.searchLomaji(query, isSearchEquals);
        } else if (mCurrentSearchType == SEARCH_TYPE_HANJI) {
            mTailoSearchAdapter.searchHanji(query, isSearchEquals);
        } else if (mCurrentSearchType == SEARCH_TYPE_HOAGI) {
            mTailoSearchAdapter.searchHoagi(query, isSearchEquals);
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
    public boolean onQueryTextChange(String query) {
        String handledQueryString = query.trim();

        if (mCurrentSearchType == SEARCH_TYPE_LOMAJI) {
            String fixedLomaji = LomajiUnicodeConverter.convertTwoCharWordToOneCharWord(query);

            if (BuildConfig.DEBUG_LOG) {
                logInputUnicode(fixedLomaji);
            }

            handledQueryString = PojToTailoConverter.convertPojMixedInputToTailoWords(fixedLomaji);
        }

        if (handledQueryString.contains(" ")) {
            handledQueryString = LomajiSearchUtils.allowSpaceInsteadOfHyphen(handledQueryString);
        }

        doSearch(handledQueryString);

        return true;
    }

    private void logInputUnicode(String query) {
        int count = query.length();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            final char c = query.charAt(i);
            String stringUnicode = Integer.toHexString((int) c);
            stringBuilder.append(stringUnicode + " ");
        }

        if (BuildConfig.DEBUG_LOG) {
            Log.d(TAG, "Input: " + query + ", Unicode: " + stringBuilder.toString());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (compoundButton == mSearchContainsRadioButton && checked) {
            mSearchContainsRadioButton.setChecked(true);
            mSearchEqualRadioButton.setChecked(false);

            mIsCurrentSearchEquals = false;

            doSearch();
        } else if (compoundButton == mSearchEqualRadioButton && checked) {
            mSearchContainsRadioButton.setChecked(false);
            mSearchEqualRadioButton.setChecked(true);

            mIsCurrentSearchEquals = true;

            doSearch();
        }
    }
}
