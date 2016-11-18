package com.taccotap.taigidict.tailo.utils;

public class TailoTaigiWordAudioUrlHelper {

    public static String getTaigiAudioUrl(int mainCode) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(TailoConstants.URL_AUDIO_FILE_PREFIX);

        String leadingZeroMainCodeString = String.format("%05d", mainCode);
        stringBuilder.append(leadingZeroMainCodeString);

        stringBuilder.append(TailoConstants.URL_AUDIO_FILE_POSTFIX);

        return stringBuilder.toString();
    }
}
