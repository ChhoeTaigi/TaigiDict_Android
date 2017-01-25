package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TlHoagiWord extends RealmObject {

    @PrimaryKey
    private int hoagiCode;

    private int mainCode;

    @Index
    @Required
    private String hoagiWord;

    public int getHoagiCode() {
        return hoagiCode;
    }

    public void setHoagiCode(int hoagiCode) {
        this.hoagiCode = hoagiCode;
    }

    public int getMainCode() {
        return mainCode;
    }

    public void setMainCode(int mainCode) {
        this.mainCode = mainCode;
    }

    public String getHoagiWord() {
        return hoagiWord;
    }

    public void setHoagiWord(String hoagiWord) {
        this.hoagiWord = hoagiWord;
    }
}
