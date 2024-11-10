package com.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

public class ManageClientThreads {

    private static HashMap<String, ServerConnectClientThread> hm =
            new HashMap<>();

    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);

    }

    public static String getOnlineFriendList() {
        Iterator<String> iterator = hm.keySet().iterator();

        String list = "";

        while (iterator.hasNext()) {
            list += iterator.next().toString() + " ";
        }

        return list;
    }

    public static void removeClientThread(String userId) {
        hm.remove(userId);
    }
}
