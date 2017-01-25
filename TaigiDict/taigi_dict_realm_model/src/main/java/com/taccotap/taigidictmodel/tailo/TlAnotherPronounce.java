package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TlAnotherPronounce extends RealmObject {

    @PrimaryKey
    private int anotherPronounceCode;

    private int mainCode;

    @Required
    private String anotherPronounceLomaji;

    private int anotherPronounceProperty;

    public int getAnotherPronounceCode() {
        return anotherPronounceCode;
    }

    public void setAnotherPronounceCode(int anotherPronounceCode) {
        this.anotherPronounceCode = anotherPronounceCode;
    }

    public int getMainCode() {
        return mainCode;
    }

    public void setMainCode(int mainCode) {
        this.mainCode = mainCode;
    }

    public String getAnotherPronounceLomaji() {
        return anotherPronounceLomaji;
    }

    public void setAnotherPronounceLomaji(String anotherPronounceLomaji) {
        this.anotherPronounceLomaji = anotherPronounceLomaji;
    }

    public int getAnotherPronounceProperty() {
        return anotherPronounceProperty;
    }

    public void setAnotherPronounceProperty(int anotherPronounceProperty) {
        this.anotherPronounceProperty = anotherPronounceProperty;
    }
}
