package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;

public class TlTaigiQuery extends RealmObject {

    private String queryWord;

    private TlTaigiWord taigiWord;

    public String getQueryWord() {
        return queryWord;
    }

    public void setQueryWord(String queryWord) {
        this.queryWord = queryWord;
    }

    public TlTaigiWord getTaigiWord() {
        return taigiWord;
    }

    public void setTaigiWord(TlTaigiWord taigiWord) {
        this.taigiWord = taigiWord;
    }
}
