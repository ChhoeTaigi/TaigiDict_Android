package com.taccotap.taigidictmodel.tailo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TlTaigiWordProperty extends RealmObject {

    @PrimaryKey
    private int propertyCode;

    @Required
    private String propertyText;

    public int getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(int propertyCode) {
        this.propertyCode = propertyCode;
    }

    public String getPropertyText() {
        return propertyText;
    }

    public void setPropertyText(String propertyText) {
        this.propertyText = propertyText;
    }
}
