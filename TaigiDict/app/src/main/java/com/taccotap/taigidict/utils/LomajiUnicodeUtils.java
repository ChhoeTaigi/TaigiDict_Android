package com.taccotap.taigidict.utils;

public class LomajiUnicodeUtils {

    // fix two-char word to one-char word
    public static String fixTwoCharWord(String unicodeLomaji) {
        String fixed = unicodeLomaji
                // x8 not change

                // a
                .replaceAll("\u0061\u0301", "\u00e1") // a2
                .replaceAll("\u0061\u0300", "\u00e0") // a3
                .replaceAll("\u0061\u0302", "\u00e2") // a5
                .replaceAll("\u0061\u0304", "\u0101") // a7

                .replaceAll("\u0041\u0301", "\u00c1") // A2
                .replaceAll("\u0041\u0300", "\u00c0") // A3
                .replaceAll("\u0041\u0302", "\u00c2") // A5
                .replaceAll("\u0041\u0304", "\u0100") // A7

                // i
                .replaceAll("\u0069\u0301", "\u00ed") // i2
                .replaceAll("\u0069\u0300", "\u00ec") // i3
                .replaceAll("\u0069\u0302", "\u00ee") // i5
                .replaceAll("\u0069\u0304", "\u012b") // i7

                .replaceAll("\u0049\u0301", "\u00cd") // I2
                .replaceAll("\u0049\u0300", "\u00cc") // I3
                .replaceAll("\u0049\u0302", "\u00ce") // I5
                .replaceAll("\u0049\u0304", "\u012a") // I7

                // u
                .replaceAll("\u0075\u0301", "\u00fa") // u2
                .replaceAll("\u0075\u0300", "\u00f9") // u3
                .replaceAll("\u0075\u0302", "\u00fb") // u5
                .replaceAll("\u0075\u0304", "\u016b") // u7

                .replaceAll("\u0055\u0301", "\u00da") // U2
                .replaceAll("\u0055\u0300", "\u00d9") // U3
                .replaceAll("\u0055\u0302", "\u00db") // U5
                .replaceAll("\u0055\u0304", "\u016a") // U7

                // e
                .replaceAll("\u0065\u0301", "\u00e9") // e2
                .replaceAll("\u0065\u0300", "\u00e8") // e3
                .replaceAll("\u0065\u0302", "\u00ea") // e5
                .replaceAll("\u0065\u0304", "\u0113") // e7

                .replaceAll("\u0045\u0301", "\u00c9") // E2
                .replaceAll("\u0045\u0300", "\u00c8") // E3
                .replaceAll("\u0045\u0302", "\u00ca") // E5
                .replaceAll("\u0045\u0304", "\u0102") // E7

                // o
                .replaceAll("\u006f\u0301", "\u00f3") // o2
                .replaceAll("\u006f\u0300", "\u00f2") // o3
                .replaceAll("\u006f\u0302", "\u00f4") // o5
                .replaceAll("\u006f\u0304", "\u014d") // o7

                .replaceAll("\u004f\u0301", "\u00d3") // O2
                .replaceAll("\u004f\u0300", "\u00d2") // O3
                .replaceAll("\u004f\u0302", "\u00d4") // O5
                .replaceAll("\u004f\u0304", "\u014c") // O7

                // n
                .replaceAll("\u006e\u0301", "\u0144") // n2
                .replaceAll("\u006e\u0300", "\u01f9") // n3; n5, n7 not change

                .replaceAll("\u004e\u0301", "\u0143") // N2
                .replaceAll("\u004e\u0300", "\u01f8") // N3; N5, N7 not change

                // m
                .replaceAll("\u006d\u0301", "\u1e3f") // m2; m3, m5, m7 not change

                .replaceAll("\u004d\u0301", "\u1e3e"); // M2; M3, M5, M7 not change

        return fixed;
    }
}
