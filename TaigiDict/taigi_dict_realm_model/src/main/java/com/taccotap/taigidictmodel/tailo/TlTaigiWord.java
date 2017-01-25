package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TlTaigiWord extends RealmObject {
    @PrimaryKey
    private int mainCode;

    private int wordPropertyCode;

    @Index
    @Required
    private String hanji;

    @Index
    @Required
    private String lomaji;

    private String wordPropertyText;

    private RealmList<TlDescription> descriptions;

    private RealmList<TlHoagiWord> hoagiWords;

    public int getMainCode() {
        return mainCode;
    }

    public void setMainCode(int mainCode) {
        this.mainCode = mainCode;
    }

    public int getWordPropertyCode() {
        return wordPropertyCode;
    }

    public void setWordPropertyCode(int wordPropertyCode) {
        this.wordPropertyCode = wordPropertyCode;
    }

    public String getHanji() {
        return hanji;
    }

    public void setHanji(String hanji) {
        this.hanji = hanji;
    }

    public String getLomaji() {
        return lomaji;
    }

    public void setLomaji(String lomaji) {
        this.lomaji = lomaji;
    }

    public String getWordPropertyText() {
        return wordPropertyText;
    }

    public void setWordPropertyText(String wordPropertyText) {
        this.wordPropertyText = wordPropertyText;
    }

    public RealmList<TlDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(RealmList<TlDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public RealmList<TlHoagiWord> getHoagiWords() {
        return hoagiWords;
    }

    public void setHoagiWords(RealmList<TlHoagiWord> hoagiWords) {
        this.hoagiWords = hoagiWords;
    }
}
