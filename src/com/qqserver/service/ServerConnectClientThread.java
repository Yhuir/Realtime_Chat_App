package com.qqserver.service;

import com.qqcommon.Message;
import com.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnectClientThread extends Thread{

    private Socket socket;
    private String userId;

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        while (true) {

            try {
                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());

                Message msg = (Message) ois.readObject();

                if (msg.getMsgType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    String onlineFriendList = ManageClientThreads.getOnlineFriendList();

                    Message message = new Message();
                    message.setMsgType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message.setContent(onlineFriendList);
                    message.setReceiver(msg.getSender());

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);


                } else if (msg.getMsgType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(msg.getSender() + " Request to exit");
                    ManageClientThreads.removeClientThread(msg.getSender());
                    socket.close();
                    break;

                } else {
                    System.out.println("Other Msg Type");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
