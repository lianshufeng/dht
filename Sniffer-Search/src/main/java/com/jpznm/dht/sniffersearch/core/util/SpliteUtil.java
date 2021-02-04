package com.jpznm.dht.sniffersearch.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SpliteUtil {

    private final static String DefaultChar = " \t\n\r\f\\/.-,;|_";

    public static String[] getToken(String info) {
        List<String> list = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(info, DefaultChar, false);
        while (stringTokenizer.hasMoreElements()) {
            list.add(stringTokenizer.nextToken());
        }
        return list.toArray(new String[0]);
    }


}
