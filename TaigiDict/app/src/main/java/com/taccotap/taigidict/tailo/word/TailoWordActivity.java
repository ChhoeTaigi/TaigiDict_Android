package com.taccotap.taigidict.tailo.word;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.taccotap.taigidict.BuildConfig;
import com.taccotap.taigidict.R;
import com.taccotap.taigidict.databinding.ActivityTailoWordBinding;
import com.taccotap.taigidict.tailo.utils.TailoDictHelper;
import com.taccotap.taigidict.tailo.utils.TailoTaigiWordAudioUrlHelper;
import com.taccotap.taigidict.utils.NetworkUtils;
import com.taccotap.taigidictmodel.tailo.TlDescription;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TailoWordActivity extends AppCompatActivity {
    private static final String TAG = TailoWordActivity.class.getSimpleName();

    @BindView(R.id.titleTextView1)
    TextView mTitleTextView1;

    @BindView(R.id.titleContentTextView1)
    TextView mTitleContentTextView1;

    @BindView(R.id.titleTextView2)
    TextView mTitleTextView2;

    @BindView(R.id.titleContentTextView2)
    TextView mTitleContentTextView2;

    @BindView(R.id.descriptionLayout)
    ViewGroup mDescriptionLayout;

    @BindView(R.id.descriptionTextView)
    TextView mDescriptionTextView;

    @BindView(R.id.wordPropertyLayout)
    ViewGroup mWordPropertyLayout;

    @BindView(R.id.wordPropertyTextView)
    TextView mWordPropertyTextView;

    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    public static final String ACTION_TAILO_WORD_MAIN_CODE = "ACTION_TAILO_WORD_MAIN_CODE";
    public static final String EXTRA_TAILO_WORD_MAIN_CODE = "EXTRA_TAILO_WORD_MAIN_CODE";

    private int mMainCode;
    private Realm mRealm;
    private ActivityTailoWordBinding mBinding;
    private MediaPlayer mMediaPlayer;
    private TlTaigiWord mTaigiWord;
    private String mVoiceUrl;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tailo_word);
        ButterKnife.bind(this);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setFonts();

        handleIntent();
    }

    private void setFonts() {
        Typeface lomajiTypeface = Typeface.createFromAsset(getAssets(), "fonts/twsong.ttf");
        mTitleContentTextView1.setTypeface(lomajiTypeface);

        Typeface hanjiTypeface = Typeface.createFromAsset(getAssets(), "fonts/twsong.ttf");
        mTitleContentTextView2.setTypeface(hanjiTypeface);
        mDescriptionTextView.setTypeface(hanjiTypeface);
        mWordPropertyTextView.setTypeface(hanjiTypeface);
    }

    private void handleIntent() {
        if (!ACTION_TAILO_WORD_MAIN_CODE.equals(getIntent().getAction())) {
            finish();
        }

        mMainCode = getIntent().getIntExtra(EXTRA_TAILO_WORD_MAIN_CODE, -1);

        if (BuildConfig.DEBUG_LOG) {
            Log.i(TAG, "mainCode=" + mMainCode);
        }

        handleCurrentWord();
    }

    private void handleCurrentWord() {
        mRealm = Realm.getDefaultInstance();

        // bind word, description
        mTaigiWord = mRealm.where(TlTaigiWord.class).equalTo("mainCode", mMainCode).findFirstAsync();
        mTaigiWord.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (BuildConfig.DEBUG_LOG) {
                    Log.d(TAG, "lomaji=" + mTaigiWord.getLomaji());
                }

                // [屬性] 附-外來詞表
                if (mTaigiWord.getWordPropertyCode() == 12) {
                    // bind 外來語
                    mTitleTextView1.setText(R.string.activity_tailo_word_title_text_1_goalaigi);
                    mTitleTextView2.setText(R.string.activity_tailo_word_title_text_2_goalaigi);
                    mTitleContentTextView1.setText(mTaigiWord.getHanji());
                    mTitleContentTextView2.setText(TailoDictHelper.getCombinatedHoagi(mTaigiWord));
                    mWordPropertyTextView.setText(mTaigiWord.getWordPropertyText());

                    // bind audio link
                    mVoiceUrl = TailoTaigiWordAudioUrlHelper.getTaigiGoaLaiAudioUrl(mMainCode);
                } else {
                    // bind word
                    mBinding.setTaigiWord(mTaigiWord);

                    if (mTaigiWord.getAnotherPronounce() != null) {
                        final String anotherPronounceLomaji = mTaigiWord.getAnotherPronounce().getAnotherPronounceLomaji();
                        mTitleContentTextView1.setText(mTaigiWord.getLomaji() + ", " + anotherPronounceLomaji);
                    } else {
                        mTitleContentTextView1.setText(mTaigiWord.getLomaji());
                    }

                    // bind audio link
                    mVoiceUrl = TailoTaigiWordAudioUrlHelper.getTaigiAudioUrl(mMainCode);
                }

                // check voice avalibility (附錄(11~22)除了外來語(12)，皆無語音檔)
                if (mTaigiWord.getWordPropertyCode() >= 11 && mTaigiWord.getWordPropertyCode() <= 22 && mTaigiWord.getWordPropertyCode() != 12) {
                    mFloatingActionButton.setVisibility(View.GONE);
                }

                // bind desc
                final RealmList<TlDescription> descriptions = mTaigiWord.getDescriptions();
                final int descriptionsCount = descriptions.size();
                if (descriptionsCount > 0) {
                    final String description = TailoDictHelper.getCombinatedDescription(mTaigiWord);
                    mDescriptionTextView.setText(description);
                    mDescriptionLayout.setVisibility(View.VISIBLE);
                } else {
                    mDescriptionLayout.setVisibility(View.GONE);
                }

                // bind word property
                final int wordPropertyCode = mTaigiWord.getWordPropertyCode();
                if (wordPropertyCode <= 2) {
                    mWordPropertyLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick(R.id.fab)
    void onClickFab() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(TailoWordActivity.this, R.string.toast_voice_need_network_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mVoiceUrl == null) {
            // TODO handle data state
            Log.e(TAG, "Data not ready yet.");
            return;
        }

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mFloatingActionButton.setImageResource(R.drawable.ic_volume_up_grey_300_36dp);
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    toastCantPlayVoiceMessage();
                    return false;
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    startPlayVoice();
                }
            });

            try {
                mMediaPlayer.setDataSource(this, Uri.parse(mVoiceUrl));
                mMediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
                toastCantPlayVoiceMessage();
            }
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.seekTo(0);
            }

            startPlayVoice();
        }
    }

    private void toastCantPlayVoiceMessage() {
        mFloatingActionButton.setImageResource(R.drawable.ic_volume_up_grey_300_36dp);
        Toast.makeText(TailoWordActivity.this, R.string.toast_sorry_cant_play, Toast.LENGTH_SHORT).show();
    }

    private void startPlayVoice() {
        mFloatingActionButton.setImageResource(R.drawable.ic_volume_up_light_green_a100_36dp);
        mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mFloatingActionButton.setImageResource(R.drawable.ic_volume_up_grey_300_36dp);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}