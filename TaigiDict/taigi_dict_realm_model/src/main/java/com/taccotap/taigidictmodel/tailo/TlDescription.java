package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TlDescription extends RealmObject {

    @PrimaryKey
    public int descCode;

    public int mainCode;

    public int descOrder;

    public int wordTypeCode;

    @Index
    @Required
    public String description;
}
