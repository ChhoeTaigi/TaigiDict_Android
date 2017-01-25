package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TlDescriptionPartOfSpeech extends RealmObject {

    @PrimaryKey
    private int partOfSpeechCode;

    private String partOfSpeechText;

    public int getPartOfSpeechCode() {
        return partOfSpeechCode;
    }

    public void setPartOfSpeechCode(int partOfSpeechCode) {
        this.partOfSpeechCode = partOfSpeechCode;
    }

    public String getPartOfSpeechText() {
        return partOfSpeechText;
    }

    public void setPartOfSpeechText(String partOfSpeechText) {
        this.partOfSpeechText = partOfSpeechText;
    }
}
