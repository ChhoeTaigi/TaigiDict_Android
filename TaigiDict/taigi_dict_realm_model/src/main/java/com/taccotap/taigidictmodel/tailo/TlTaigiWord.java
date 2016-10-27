package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TlTaigiWord extends RealmObject {
    @PrimaryKey
    public int mainCode;

    public int propertyCode;

    @Index
    @Required
    public String hanji;

    @Index
    @Required
    public String lomaji;
}
