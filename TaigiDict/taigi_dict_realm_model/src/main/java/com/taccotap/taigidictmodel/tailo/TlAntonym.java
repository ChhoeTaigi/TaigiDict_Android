package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TlAntonym extends RealmObject {

    @PrimaryKey
    private int antonymCode;

    private int mainCode;

    private int antonymMainCode;

    private String lomaji;

    public int getAntonymCode() {
        return antonymCode;
    }

    public void setAntonymCode(int antonymCode) {
        this.antonymCode = antonymCode;
    }

    public int getMainCode() {
        return mainCode;
    }

    public void setMainCode(int mainCode) {
        this.mainCode = mainCode;
    }

    public int getAntonymMainCode() {
        return antonymMainCode;
    }

    public void setAntonymMainCode(int antonymMainCode) {
        this.antonymMainCode = antonymMainCode;
    }

    public String getLomaji() {
        return lomaji;
    }

    public void setLomaji(String lomaji) {
        this.lomaji = lomaji;
    }
}
