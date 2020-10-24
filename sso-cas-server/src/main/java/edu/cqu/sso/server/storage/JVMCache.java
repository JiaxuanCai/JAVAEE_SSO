package edu.cqu.sso.server.storage;

import edu.cqu.sso.server.model.TicketGrangtingTicket;

import java.util.HashMap;
import java.util.Map;

public class JVMCache {

    public static Map<String, String> ST_CACHE = new HashMap<String, String>();
    public static Map<String, TicketGrangtingTicket> TGT_CACHE = new HashMap<>();
    
}