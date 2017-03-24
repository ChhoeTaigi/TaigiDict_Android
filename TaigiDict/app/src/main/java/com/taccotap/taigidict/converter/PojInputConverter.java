package com.taccotap.taigidict.converter;

import android.util.Log;

import com.taccotap.taigidict.BuildConfig;

public class PojInputConverter {
    private static final String TAG = PojInputConverter.class.getSimpleName();

    public static String convertPojMixedInputToPojNumberWords(String input) {
        if (BuildConfig.DEBUG_LOG) {
            Log.d(TAG, "convertPojMixedInputToPojNumberWords(): input=" + input);
        }

        if (input == null) {
            return input;
        }

        StringBuilder stringBuilder = new StringBuilder();
        final String[] words = input.split("[ -]");
        for (String foundTaigiWord : words) {
            String pojNumber = LomajiConverter.convertToNumberTone(foundTaigiWord, LomajiConverter.LOMAJI_TYPE_POJ);
            if (BuildConfig.DEBUG_LOG) {
                Log.d(TAG, "foundTaigiWord=" + foundTaigiWord + ", pojNumber=" + pojNumber);
            }

            stringBuilder.append(pojNumber);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }
}
