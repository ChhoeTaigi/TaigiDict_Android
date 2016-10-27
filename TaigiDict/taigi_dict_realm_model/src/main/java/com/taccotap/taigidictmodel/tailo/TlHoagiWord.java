package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TlHoagiWord extends RealmObject {

    @PrimaryKey
    public int hoagiCode;

    public int mainCode;

    @Index
    @Required
    public String hoagiWord;
}
