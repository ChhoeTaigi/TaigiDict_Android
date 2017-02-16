package com.taccotap.taigidict.utils;


public class LomajiSearchUtils {

    public static final String allowSpaceInsteadOfHyphen(String queryString) {
        return queryString.trim().replaceAll(" +", " ").replaceAll(" ", "-");
    }
}
