package com.taccotap.taigidict.tailo.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;

import com.taccotap.taigidict.R;
import com.taccotap.taigidict.databinding.ListitemTailoSearchBinding;
import com.taccotap.taigidict.tailo.utils.TailoConstants;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;

import io.reactivex.processors.PublishProcessor;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.Sort;

public class TailoSearchAdapter extends RealmRecyclerViewAdapter<TlTaigiWord, TailoSearchAdapter.TailoSearchViewHolder> {
    private static final String TAG = TailoSearchAdapter.class.getSimpleName();

    private final Realm mRealm;

    private final PublishProcessor<Integer> mOnClickSubject = PublishProcessor.create();

    public TailoSearchAdapter(Context context, Realm realm) {
        super(context, realm.where(TlTaigiWord.class).contains("lomaji", TailoConstants.DEFAULT_QUERY_STRING).findAllAsync(), false);
        mRealm = realm;
    }

    public PublishProcessor<Integer> getItemClickProcessor() {
        return mOnClickSubject;
    }

    @Override
    public TailoSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListitemTailoSearchBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.listitem_tailo_search, parent, false);
        return new TailoSearchViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(TailoSearchViewHolder holder, final int position) {
        TlTaigiWord tlTaigiWord = getData().get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickSubject.onNext(position);
            }
        });

        holder.dataBinding.setTaigiWord(tlTaigiWord);
        holder.dataBinding.executePendingBindings();
    }

    public TlTaigiWord getItem(int position) {
        return this.getData().get(position);
    }

    public class TailoSearchViewHolder extends RealmViewHolder {
        public final ListitemTailoSearchBinding dataBinding;

        public TailoSearchViewHolder(ListitemTailoSearchBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }
    }

    public void searchLomaji(String lomaji, boolean isSearchEquals) {
        final RealmResults<TlTaigiWord> realmResults;

        RealmQuery<TlTaigiWord> where = mRealm.where(TlTaigiWord.class);
        if (!isSearchEquals) {
            where = where.contains("lomaji", lomaji, Case.INSENSITIVE);
        } else {
            where = where.equalTo("lomaji", lomaji, Case.INSENSITIVE);
        }

        realmResults = where.findAllSortedAsync("lomaji", Sort.ASCENDING);

        realmResults.addChangeListener(new RealmChangeListener<RealmResults<TlTaigiWord>>() {
            @Override
            public void onChange(RealmResults<TlTaigiWord> element) {
                updateData(realmResults);
            }
        });
    }

    public void searchHoagi(String hoagi, boolean isSearchEquals) {
        final RealmResults<TlTaigiWord> realmResults;
        RealmQuery<TlTaigiWord> where = mRealm.where(TlTaigiWord.class);
        if (!isSearchEquals) {
            where = where.contains("hoagiWords.hoagiWord", hoagi);
        } else {
            where = where.equalTo("hoagiWords.hoagiWord", hoagi);
        }

        realmResults = where.findAllSortedAsync("mainCode", Sort.ASCENDING);

        realmResults.addChangeListener(new RealmChangeListener<RealmResults<TlTaigiWord>>() {
            @Override
            public void onChange(RealmResults<TlTaigiWord> element) {
                updateData(realmResults);
            }
        });
    }

    public void searchAll(String query) {
        final RealmResults<TlTaigiWord> realmResults;
        RealmQuery<TlTaigiWord> where = mRealm.where(TlTaigiWord.class);

        where = where.contains("lomaji", query, Case.INSENSITIVE)
                .or().contains("descriptions.description", query)
                .or().contains("descriptions.exampleSentences.exampleSentenceHanji", query)
                .or().contains("descriptions.exampleSentences.exampleSentenceLomaji", query)
                .or().contains("descriptions.exampleSentences.exampleSentenceHoagi", query);

        realmResults = where.findAllSortedAsync("mainCode", Sort.ASCENDING);

        realmResults.addChangeListener(new RealmChangeListener<RealmResults<TlTaigiWord>>() {
            @Override
            public void onChange(RealmResults<TlTaigiWord> element) {
                updateData(realmResults);
            }
        });
    }
}
