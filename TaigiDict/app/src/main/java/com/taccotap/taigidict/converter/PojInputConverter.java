package com.taccotap.taigidict.converter;

import android.text.TextUtils;
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
            String pojNumber = convertPojWordToPojNumberWord(foundTaigiWord);
            if (BuildConfig.DEBUG_LOG) {
                Log.d(TAG, "foundTaigiWord=" + foundTaigiWord + ", pojNumber=" + pojNumber);
            }

            stringBuilder.append(pojNumber);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    private static final String convertPojWordToPojNumberWord(String poj) {
        if (TextUtils.isEmpty(poj)) {
            return poj;
        }

        StringBuilder stringBuilder = new StringBuilder();

        boolean hasFoundUnicodeToneChar = false;
        int length = poj.length();
        for (int i = 0; i < length; i++) {
            final String currentCharString = poj.substring(i, i + 1);

            final String currentPojNumber = Poj.sPojUnicodeToPojNumberHashMap.get(currentCharString);
            if (currentPojNumber != null) {
                stringBuilder.append(currentPojNumber);
                hasFoundUnicodeToneChar = true;
            } else {
                if (hasFoundUnicodeToneChar && TextUtils.isDigitsOnly(currentCharString)) {
                    // remove useless ending numbers, skip
                } else {
                    stringBuilder.append(currentCharString);
                }
            }
        }

        final String pojNumberInTheMiddle = stringBuilder.toString();

        stringBuilder = new StringBuilder();
        length = pojNumberInTheMiddle.length();
        String number = null;
        for (int i = 0; i < length; i++) {
            final String currentCharString = pojNumberInTheMiddle.substring(i, i + 1);

            // only find the closest number, ignore other ending numbers
            if (TextUtils.isDigitsOnly(currentCharString)) {
                if (number == null) {
                    number = currentCharString;
                }
            } else {
                stringBuilder.append(currentCharString);
            }
        }

        if (number != null) {
            stringBuilder.append(number);
        }

        return stringBuilder.toString();
    }
}
