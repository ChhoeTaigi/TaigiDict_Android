package com.taccotap.taigidict.tailo.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taccotap.taigidict.R;
import com.taccotap.taigidict.databinding.ListitemTailoSearchBinding;
import com.taccotap.taigidict.tailo.utils.TailoDictHelper;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;

import java.util.ArrayList;

import io.reactivex.processors.PublishProcessor;

public class TailoSearchAdapter extends RecyclerView.Adapter<TailoSearchAdapter.TailoSearchViewHolder> {
    private static final String TAG = TailoSearchAdapter.class.getSimpleName();

    private final Context mContext;

    private ArrayList<TlTaigiWord> mTaigiWords = new ArrayList<>();

    private final PublishProcessor<Integer> mOnClickSubject = PublishProcessor.create();
    private LayoutInflater mLayoutInflater;

    public class TailoSearchViewHolder extends RecyclerView.ViewHolder {
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

    public TailoSearchAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setTaigiWords(ArrayList<TlTaigiWord> taigiWords) {
        mTaigiWords.clear();
        mTaigiWords.addAll(taigiWords);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTaigiWords.size();
    }

    public PublishProcessor<Integer> getItemClickProcessor() {
        return mOnClickSubject;
    }

    @Override
    public TailoSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListitemTailoSearchBinding dataBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout.listitem_tailo_search, parent, false);

        final TailoSearchViewHolder viewHolder = new TailoSearchViewHolder(dataBinding);

        Typeface lomajiTypeface = Typeface.createFromAsset(viewHolder.itemView.getContext().getAssets(), "fonts/twsong.ttf");
        viewHolder.titleContentTextView1.setTypeface(lomajiTypeface);
        Typeface hanjiTypeface = Typeface.createFromAsset(viewHolder.itemView.getContext().getAssets(), "fonts/twsong.ttf");
        viewHolder.titleContentTextView2.setTypeface(hanjiTypeface);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TailoSearchViewHolder viewHolder, final int position) {
        TlTaigiWord tlTaigiWord = getItem(position);

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

            if (tlTaigiWord.getAnotherPronounce() != null) {
                final String anotherPronounceLomaji = tlTaigiWord.getAnotherPronounce().getAnotherPronounceLomaji();
                viewHolder.titleContentTextView1.setText(viewHolder.titleContentTextView1.getText() + ", " + anotherPronounceLomaji);
            }
        }
    }

    public TlTaigiWord getItem(int position) {
        return mTaigiWords.get(position);
    }


}
