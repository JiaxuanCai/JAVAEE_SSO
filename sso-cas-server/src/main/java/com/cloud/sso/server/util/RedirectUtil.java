package com.cloud.sso.server.util;

public class RedirectUtil {
    public static String getRedirectUrlWithTicket(String service, String ST){
        StringBuilder url = new StringBuilder();
        url.append(service);
        if (service.contains("?")) {
            url.append("&");
        } else {
            url.append("?");
        }
        url.append("ticket=").append(ST);
        return String.valueOf(url);
    }
}
