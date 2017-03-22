package com.taccotap.taigidict.tailo.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taccotap.taigidict.R;
import com.taccotap.taigidict.databinding.ListitemTailoSearchBinding;
import com.taccotap.taigidict.tailo.utils.TailoConstants;
import com.taccotap.taigidict.tailo.utils.TailoDictHelper;
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

    public class TailoSearchViewHolder extends RealmViewHolder {
        public final ListitemTailoSearchBinding dataBinding;
        public TextView titleTextView1;
        public TextView titleContentTextView1;
        public TextView titleTextView2;
        public TextView titleContentTextView2;

        public TailoSearchViewHolder(ListitemTailoSearchBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            this.titleTextView1 = (TextView) dataBinding.getRoot().findViewById(R.id.titleTextView1);
            this.titleContentTextView1 = (TextView) dataBinding.getRoot().findViewById(R.id.titleContentTextView1);
            this.titleTextView2 = (TextView) dataBinding.getRoot().findViewById(R.id.titleTextView2);
            this.titleContentTextView2 = (TextView) dataBinding.getRoot().findViewById(R.id.titleContentTextView2);
        }
    }

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

        final TailoSearchViewHolder viewHolder = new TailoSearchViewHolder(dataBinding);

        Typeface lomajiTypeface = Typeface.createFromAsset(viewHolder.itemView.getContext().getAssets(), "fonts/twu3.ttf");
        viewHolder.titleContentTextView1.setTypeface(lomajiTypeface);
        Typeface hanjiTypeface = Typeface.createFromAsset(viewHolder.itemView.getContext().getAssets(), "fonts/mingliub.ttc");
        viewHolder.titleContentTextView2.setTypeface(hanjiTypeface);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TailoSearchViewHolder viewHolder, final int position) {
        TlTaigiWord tlTaigiWord = getData().get(position);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickSubject.onNext(position);
            }
        });

        // [屬性] 附-外來詞表
        if (tlTaigiWord.getWordPropertyCode() == 12) {
            viewHolder.titleTextView1.setText(R.string.listitem_tailo_search_title_text_1_goalaigi);
            viewHolder.titleTextView2.setText(R.string.listitem_tailo_search_title_text_2_goalaigi);

            viewHolder.titleContentTextView1.setText(tlTaigiWord.getHanji());
            viewHolder.titleContentTextView2.setText(TailoDictHelper.getCombinatedHoagi(tlTaigiWord));
        } else {
            viewHolder.titleTextView1.setText(R.string.listitem_tailo_search_title_text_1_tailo);
            viewHolder.titleTextView2.setText(R.string.listitem_tailo_search_title_text_2_tailo);

            viewHolder.dataBinding.setTaigiWord(tlTaigiWord);
            viewHolder.dataBinding.executePendingBindings();
        }
    }

    public TlTaigiWord getItem(int position) {
        return this.getData().get(position);
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

    public void searchHanji(String hanji, boolean isSearchEquals) {
        final RealmResults<TlTaigiWord> realmResults;

        RealmQuery<TlTaigiWord> where = mRealm.where(TlTaigiWord.class);
        if (!isSearchEquals) {
            where = where.contains("hanji", hanji, Case.INSENSITIVE);
        } else {
            where = where.equalTo("hanji", hanji, Case.INSENSITIVE);
        }

        realmResults = where.findAllSortedAsync("hanji", Sort.ASCENDING);

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
                .or().contains("hanji", query, Case.INSENSITIVE)
                .or().contains("hoagiWords.hoagiWord", query)
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
