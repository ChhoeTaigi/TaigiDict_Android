package com.taccotap.taigidict.tailo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import com.taccotap.taigidict.R;
import com.taccotap.taigidict.databinding.ListitemTlSearchBinding;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.Sort;

public class TlSearchAdapter extends RealmBasedRecyclerViewAdapter<TlTaigiWord, TlSearchAdapter.TlSearchViewHolder> {
    private static final String TAG = TlSearchAdapter.class.getSimpleName();

    private final Context mContext;
    private final Realm mRealm;

    private String filterKey = "lomaji";
    private boolean useContains = true;
    private Case casing = Case.INSENSITIVE;
    private Sort sortOrder = Sort.ASCENDING;
    private String sortKey = filterKey;
    private String basePredicate = null;

    public class TlSearchViewHolder extends RealmViewHolder {
        public final ListitemTlSearchBinding dataBinding;

        public TlSearchViewHolder(ListitemTlSearchBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }
    }

    public TlSearchAdapter(Context context, Realm realm) {
        super(context, null, false, false);
        mContext = context;
        mRealm = realm;
    }

    @Override
    public TlSearchViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int position) {
        final ListitemTlSearchBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.listitem_tl_search, viewGroup, false);
        return new TlSearchViewHolder(dataBinding);
    }

    @Override
    public void onBindRealmViewHolder(TlSearchViewHolder holder, int position) {
        TlTaigiWord tlTaigiWord = realmResults.get(position);

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
}
