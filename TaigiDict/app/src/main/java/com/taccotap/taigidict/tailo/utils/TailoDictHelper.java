package com.taccotap.taigidict.tailo.utils;


import android.text.TextUtils;

import com.taccotap.taigidictmodel.tailo.TlDescription;
import com.taccotap.taigidictmodel.tailo.TlExampleSentence;
import com.taccotap.taigidictmodel.tailo.TlHoagiWord;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;

import io.realm.RealmList;

public class TailoDictHelper {

    public static String getCombinatedHoagi(TlTaigiWord taigiWord) {
        final RealmList<TlHoagiWord> hoagiWords = taigiWord.getHoagiWords();
        final int count = hoagiWords.size();

        if (count == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            final TlHoagiWord hoagiWord = hoagiWords.get(i);

            stringBuilder.append(hoagiWord.getHoagiWord());

            if (i < count - 1) {
                stringBuilder.append("、");
            }
        }

        return stringBuilder.toString();
    }

    public static String getCombinatedDescription(TlTaigiWord taigiWord) {
        final RealmList<TlDescription> descriptions = taigiWord.getDescriptions();
        final int descriptionsCount = descriptions.size();

        if (descriptionsCount == 0) {
            return "";
        }

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

        return stringBuilder.toString();
    }
}
