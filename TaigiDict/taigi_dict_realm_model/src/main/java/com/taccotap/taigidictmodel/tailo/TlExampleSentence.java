package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TlExampleSentence extends RealmObject {

    @PrimaryKey
    private int exampleSentenceCode;

    private int mainCode;

    private int descCode;

    private int exampleSentenceOrder;

    @Index
    @Required
    private String exampleSentenceHanji;

    @Index
    @Required
    private String exampleSentenceLomaji;

    @Index
    @Required
    private String exampleSentenceHoagi;

    public int getExampleSentenceCode() {
        return exampleSentenceCode;
    }

    public void setExampleSentenceCode(int exampleSentenceCode) {
        this.exampleSentenceCode = exampleSentenceCode;
    }

    public int getMainCode() {
        return mainCode;
    }

    public void setMainCode(int mainCode) {
        this.mainCode = mainCode;
    }

    public int getDescCode() {
        return descCode;
    }

    public void setDescCode(int descCode) {
        this.descCode = descCode;
    }

    public int getExampleSentenceOrder() {
        return exampleSentenceOrder;
    }

    public void setExampleSentenceOrder(int exampleSentenceOrder) {
        this.exampleSentenceOrder = exampleSentenceOrder;
    }

    public String getExampleSentenceHanji() {
        return exampleSentenceHanji;
    }

    public void setExampleSentenceHanji(String exampleSentenceHanji) {
        this.exampleSentenceHanji = exampleSentenceHanji;
    }

    public String getExampleSentenceLomaji() {
        return exampleSentenceLomaji;
    }

    public void setExampleSentenceLomaji(String exampleSentenceLomaji) {
        this.exampleSentenceLomaji = exampleSentenceLomaji;
    }

    public String getExampleSentenceHoagi() {
        return exampleSentenceHoagi;
    }

    public void setExampleSentenceHoagi(String exampleSentenceHoagi) {
        this.exampleSentenceHoagi = exampleSentenceHoagi;
    }
}
