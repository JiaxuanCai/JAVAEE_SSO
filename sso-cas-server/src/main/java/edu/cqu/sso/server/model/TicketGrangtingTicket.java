package edu.cqu.sso.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketGrangtingTicket {

    String username;
    public Map<String, String> serviceMap = new HashMap<String, String>();

    public TicketGrangtingTicket(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
