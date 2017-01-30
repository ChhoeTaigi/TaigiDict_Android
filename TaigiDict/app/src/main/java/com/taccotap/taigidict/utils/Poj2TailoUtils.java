package com.taccotap.taigidict.utils;

public class Poj2TailoUtils {

    public static final String poj2tailo(String poj) {
        String tailo = poj.replaceAll("ch", "ts") // ch -> ts
                .replaceAll("Ch", "Ts") // Ch -> Ts
                .replaceAll("\u207f", "nn") // ⁿ -> nn
                .replaceAll("o(.?)\u0358", "o$1o") // o͘ -> oo, ó͘ -> óo, and etc.
                .replaceAll("O(.?)\u0358", "O$1o") // o͘ -> oo, ó͘ -> óo, and etc.
                .replaceAll("o([aáàâāa̍AÁÀÂĀA̍eéèêēe̍EÉÈÊĒE̍])", "u$1") // oa -> ua, oe -> ue, and etc.
                .replaceAll("O([aáàâāa̍AÁÀÂĀA̍eéèêēe̍EÉÈÊĒE̍])", "U$1") // Oa -> Ua, Oe -> Ue, and etc.
                .replaceAll("ek", "ik") // ek -> ik
                .replaceAll("Ek", "Ik") // Ek -> Ik
                .replaceAll("e\u030dk", "i\u030dk") // e̍k -> i̍k
                .replaceAll("E\u030dk", "I\u030dk") // E̍k -> I̍k
                .replaceAll("eng", "ing") // eng -> ing
                .replaceAll("Eng", "Ing") // Eng -> Ing
                .replaceAll("e\u030dng", "i\u030dng") // e̍ng -> i̍ng
                .replaceAll("E\u030dng", "I\u030dng"); // E̍ng -> I̍ng
        return tailo;
    }
}
