package com.taccotap.taigidict.tailo.word;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.taccotap.taigidict.R;
import com.taccotap.taigidict.databinding.ActivityTailoWordBinding;
import com.taccotap.taigidict.tailo.utils.TailoTaigiWordAudioUrlHelper;
import com.taccotap.taigidictmodel.tailo.TlDescription;
import com.taccotap.taigidictmodel.tailo.TlExampleSentence;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;

public class TailoWordActivity extends AppCompatActivity {
    private static final String TAG = TailoWordActivity.class.getSimpleName();

    @BindView(R.id.descriptionLayout)
    ViewGroup mDescriptionLayout;

    @BindView(R.id.descriptionTextView)
    TextView mDescriptionTextView;

    @BindView(R.id.wordPropertyLayout)
    ViewGroup mWordPropertyLayout;

    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    public static final String ACTION_TAILO_WORD_MAIN_CODE = "ACTION_TAILO_WORD_MAIN_CODE";
    public static final String EXTRA_TAILO_WORD_MAIN_CODE = "EXTRA_TAILO_WORD_MAIN_CODE";

    private int mMainCode;
    private Realm mRealm;
    private ActivityTailoWordBinding mBinding;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tailo_word);
        ButterKnife.bind(this);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        handleIntent();
    }

    private void handleIntent() {
        if (!ACTION_TAILO_WORD_MAIN_CODE.equals(getIntent().getAction())) {
            finish();
        }

        mMainCode = getIntent().getIntExtra(EXTRA_TAILO_WORD_MAIN_CODE, -1);

        Log.i(TAG, "mainCode=" + mMainCode);

        handleCurrentWord();
    }

    private void handleCurrentWord() {
        mRealm = Realm.getDefaultInstance();

        // bind word, description
        final TlTaigiWord taigiWord = mRealm.where(TlTaigiWord.class).equalTo("mainCode", mMainCode).findFirstAsync();
        taigiWord.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                Log.i(TAG, "lomaji=" + taigiWord.getLomaji());

                // bind word
                mBinding.setTaigiWord(taigiWord);

                // bind desc
                final RealmList<TlDescription> descriptions = taigiWord.getDescriptions();
                final int descriptionsCount = descriptions.size();
                if (descriptionsCount > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i <= descriptionsCount; i++) {
                        final TlDescription description = descriptions.get(i - 1);

                        if (descriptionsCount > 1) {
                            stringBuilder.append(i + ".");
                        }

                        final String partOfSpeech = description.getPartOfSpeech();
                        if (!TextUtils.isEmpty(partOfSpeech)) {
                            stringBuilder.append("【" + partOfSpeech + "】");
                        } else {
                            if (descriptionsCount > 1) {
                                stringBuilder.append(" ");
                            }
                        }

                        stringBuilder.append(description.getDescription());

                        final RealmList<TlExampleSentence> exampleSentences = description.getExampleSentences();
                        final int exampleSentencesCount = exampleSentences.size();
                        for (int j = 0; j < exampleSentencesCount; j++) {
                            final TlExampleSentence currentExampleSentence = exampleSentences.get(j);

                            if (j == 0) {
                                stringBuilder.append("例：");
                            }

                            stringBuilder.append(currentExampleSentence.getExampleSentenceLomaji());
                            stringBuilder.append(" ");
                            stringBuilder.append(currentExampleSentence.getExampleSentenceHanji());

                            if (!TextUtils.isEmpty(currentExampleSentence.getExampleSentenceHoagi())) {
                                stringBuilder.append(" (");
                                stringBuilder.append(currentExampleSentence.getExampleSentenceHoagi());
                                stringBuilder.append(")");
                            }

                            if (j < exampleSentencesCount - 1) {
                                stringBuilder.append("；");
                            }
                        }

                        if (i < descriptionsCount) {
                            stringBuilder.append(System.getProperty("line.separator"));
                        }
                    }

                    mDescriptionTextView.setText(stringBuilder.toString());
                } else {
                    mDescriptionLayout.setVisibility(View.GONE);
                }

                // bind word property
                final int wordPropertyCode = taigiWord.getWordPropertyCode();
                if (wordPropertyCode <= 2) {
                    mWordPropertyLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick(R.id.fab)
    void onClickFab() {
        if (!isNetworkAvailable()) {
            Toast.makeText(TailoWordActivity.this, R.string.toast_voice_need_network_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(this, Uri.parse(TailoTaigiWordAudioUrlHelper.getTaigiAudioUrl(mMainCode)));
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mFloatingActionButton.setImageResource(R.drawable.ic_volume_up_grey_300_36dp);
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    mFloatingActionButton.setImageResource(R.drawable.ic_volume_up_grey_300_36dp);
                    Toast.makeText(TailoWordActivity.this, R.string.toast_sorry_cant_play, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(0);
        }
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}