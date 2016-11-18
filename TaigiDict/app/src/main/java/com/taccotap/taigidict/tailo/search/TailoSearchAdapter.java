package com.taccotap.taigidict.tailo.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;

import com.taccotap.taigidict.R;
import com.taccotap.taigidict.databinding.ListitemTailoSearchBinding;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;

import io.reactivex.processors.PublishProcessor;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.Sort;

public class TailoSearchAdapter extends RealmBasedRecyclerViewAdapter<TlTaigiWord, TailoSearchAdapter.TailoSearchViewHolder> {
    private static final String TAG = TailoSearchAdapter.class.getSimpleName();

    private final Context mContext;
    private final Realm mRealm;

    private String filterKey = "lomaji";
    private boolean useContains = true;
    private Case casing = Case.INSENSITIVE;
    private Sort sortOrder = Sort.ASCENDING;
    private String sortKey = filterKey;
    private String basePredicate = null;

    private final PublishProcessor<Integer> mOnClickSubject = PublishProcessor.create();

    public TailoSearchAdapter(Context context, Realm realm) {
        super(context, null, false, false);
        mContext = context;
        mRealm = realm;
    }

    public PublishProcessor<Integer> getItemClickProcessor() {
        return mOnClickSubject;
    }

    @Override
    public TailoSearchViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int position) {
        final ListitemTailoSearchBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.listitem_tailo_search, viewGroup, false);
        return new TailoSearchViewHolder(dataBinding);
    }

    @Override
    public void onBindRealmViewHolder(TailoSearchViewHolder holder, final int position) {
        TlTaigiWord tlTaigiWord = realmResults.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickSubject.onNext(position);
            }
        });

        holder.dataBinding.setTaigiWord(tlTaigiWord);
        holder.dataBinding.executePendingBindings();
    }

    public void search(String input) {
        RealmResults<TlTaigiWord> businesses;
        RealmQuery<TlTaigiWord> where = mRealm.where(TlTaigiWord.class);
        if (input.isEmpty() && basePredicate != null) {
            if (useContains) {
                where = where.contains(filterKey, basePredicate, casing);
            } else {
                where = where.beginsWith(filterKey, basePredicate, casing);
            }
        } else if (!input.isEmpty()) {
            if (useContains) {
                where = where.contains(filterKey, input, casing);
            } else {
                where = where.beginsWith(filterKey, input, casing);
            }
        }

        if (sortKey == null) {
            businesses = where.findAll();
        } else {
            businesses = where.findAllSorted(sortKey, sortOrder);
        }

        updateRealmResults(businesses);
    }

    public TlTaigiWord getItem(int position) {
        return this.realmResults.get(position);
    }

    public class TailoSearchViewHolder extends RealmViewHolder {
        public final ListitemTailoSearchBinding dataBinding;

        public TailoSearchViewHolder(ListitemTailoSearchBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }
    }
}
