package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TlThesaurus extends RealmObject {

    @PrimaryKey
    private int thesaurusCode;

    private int mainCode;

    private int thesaurusMainCode;

    private String lomaji;

    public int getThesaurusCode() {
        return thesaurusCode;
    }

    public void setThesaurusCode(int thesaurusCode) {
        this.thesaurusCode = thesaurusCode;
    }

    public int getMainCode() {
        return mainCode;
    }

    public void setMainCode(int mainCode) {
        this.mainCode = mainCode;
    }

    public int getThesaurusMainCode() {
        return thesaurusMainCode;
    }

    public void setThesaurusMainCode(int thesaurusMainCode) {
        this.thesaurusMainCode = thesaurusMainCode;
    }

    public String getLomaji() {
        return lomaji;
    }

    public void setLomaji(String lomaji) {
        this.lomaji = lomaji;
    }
}
