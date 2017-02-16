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
                .replaceAll("\u00e9ng", "\u00edng") // éng -> íng
                .replaceAll("\u00c9ng", "\u00cdng") // Éng -> Íng
                .replaceAll("\u00e8ng", "\u00ecng") // èng -> ìng
                .replaceAll("\u00c8ng", "\u00ccng") // Èng -> Ìng
                .replaceAll("\u00eang", "\u00eeng") // êng -> îng
                .replaceAll("\u00cang", "\u00ceng") // Êng -> Îng
                .replaceAll("\u0113ng", "\u012bng") // ēng -> īng
                .replaceAll("\u0102ng", "\u012ang") // Ēng -> Īng
                .replaceAll("e\u030dng", "i\u030dng") // e̍ng -> i̍ng
                .replaceAll("E\u030dng", "I\u030dng"); // E̍ng -> I̍ng
        return tailo;
    }
}
