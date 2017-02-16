package com.taccotap.taigidict.tailo.utils;

import java.util.Locale;

public class TailoTaigiWordAudioUrlHelper {

    public static String getTaigiAudioUrl(int mainCode) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(TailoConstants.URL_AUDIO_FILE_PREFIX);

        String leadingZeroMainCodeString = String.format(Locale.ENGLISH, "%05d", mainCode);
        stringBuilder.append(leadingZeroMainCodeString);

        stringBuilder.append(TailoConstants.URL_AUDIO_FILE_POSTFIX);

        return stringBuilder.toString();
    }

    public static String getTaigiGoaLaiAudioUrl(int mainCode) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(TailoConstants.URL_AUDIO_FILE_PREFIX + TailoConstants.URL_NODE_WAILAI);

        String leadingZeroMainCodeString = String.format(Locale.ENGLISH, "%d", mainCode - 31000);
        stringBuilder.append(leadingZeroMainCodeString);

        stringBuilder.append(TailoConstants.URL_AUDIO_FILE_POSTFIX);

        return stringBuilder.toString();
    }
}
