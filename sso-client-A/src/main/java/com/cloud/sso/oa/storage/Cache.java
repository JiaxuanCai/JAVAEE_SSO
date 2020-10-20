package com.cloud.sso.oa.storage;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class Cache {
    public static Map<String, String> map = new HashMap<>();
    public static Map<String, HttpSession> sessionMap = new HashMap<>();
}
