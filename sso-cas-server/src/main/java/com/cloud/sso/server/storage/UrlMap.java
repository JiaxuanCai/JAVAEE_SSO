package com.cloud.sso.server.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlMap {
    public static Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    public static void set(String token, String url) {
        if (!map.containsKey(token)) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(url);
            map.put(token, list);
            return;
        }
        map.get(token).add(url);
    }

    public static List<String> pop(String token) {
        if (map.containsKey(token)) {
            return map.remove(token);
        }
        return null;
    }
}
