package com.qqserver.service;

import com.qqcommon.Message;
import com.qqcommon.MessageType;
import com.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class QQServer {

    private ServerSocket ss = null;

    private static ConcurrentHashMap<String, User> validUser = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<Message>> offlineUser = new ConcurrentHashMap<>();

    static {
        validUser.put("100", new User("100", "123456"));
        validUser.put("200", new User("200", "123456"));
        validUser.put("300", new User("300", "123456"));
        validUser.put("400", new User("400", "123456"));
        validUser.put("500", new User("500", "123456"));

    }

    public boolean checkUser(String userId, String passwd) {
        User user = validUser.get(userId);

        if (user == null) {
            return false;
        }

        if (! user.getPasswd().equals(passwd)) {
            return false;
        }

        return true;
    }



    public QQServer() {

        try {

            System.out.println("Listen on 9999");
            ss = new ServerSocket(9999);

            new Thread(new SendNewsToAllService()).start();

            while (true) {
                Socket socket = ss.accept();

                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());

                ObjectOutputStream oos =
                        new ObjectOutputStream(socket.getOutputStream());

                User u = (User) ois.readObject();

                Message message = new Message();

                if (checkUser(u.getUserId(), u.getPasswd())) {
                    System.out.println("\nUser: " + u.getUserId() + " Logged in");

                    message.setMsgType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);

                    ServerConnectClientThread scct =
                            new ServerConnectClientThread(socket, u.getUserId());

                    scct.start();

                    ManageClientThreads.addClientThread(u.getUserId(), scct);


                } else {
                    //fail
                    System.out.println("login not match!");

                    message.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);

                    socket.close();

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
