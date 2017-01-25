package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TlDescription extends RealmObject {

    @PrimaryKey
    private int descCode;

    private int mainCode;

    private int descOrder;

    private int partOfSpeechCode;

    @Index
    @Required
    private String description;

    private String partOfSpeech;

    private RealmList<TlExampleSentence> exampleSentences;

    public int getDescCode() {
        return descCode;
    }

    public void setDescCode(int descCode) {
        this.descCode = descCode;
    }

    public int getMainCode() {
        return mainCode;
    }

    public void setMainCode(int mainCode) {
        this.mainCode = mainCode;
    }

    public int getDescOrder() {
        return descOrder;
    }

    public void setDescOrder(int descOrder) {
        this.descOrder = descOrder;
    }

    public int getPartOfSpeechCode() {
        return partOfSpeechCode;
    }

    public void setPartOfSpeechCode(int partOfSpeechCode) {
        this.partOfSpeechCode = partOfSpeechCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public RealmList<TlExampleSentence> getExampleSentences() {
        return exampleSentences;
    }

    public void setExampleSentences(RealmList<TlExampleSentence> exampleSentences) {
        this.exampleSentences = exampleSentences;
    }
}
