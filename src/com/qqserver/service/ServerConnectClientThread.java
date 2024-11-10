package com.qqserver.service;

import com.qqcommon.Message;
import com.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

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

                } else if (msg.getMsgType().equals(MessageType.MESSAGE_COMM_MESS)) {
                    String receiveId = msg.getReceiver();

                    ServerConnectClientThread scct = ManageClientThreads.getServerConnectClientThread(receiveId);

                    ObjectOutputStream oos = new ObjectOutputStream(scct.socket.getOutputStream());

                    oos.writeObject(msg);

                } else if (msg.getMsgType().equals(MessageType.MESSAGE_All_MESS)) {
                    HashMap hm = ManageClientThreads.getHm();

                    Iterator<String> iterator = hm.keySet().iterator();

                    while (iterator.hasNext()) {
                        String receiverId = iterator.next().toString();

                        if (!receiverId.equals(msg.getSender())) {

                            ServerConnectClientThread scct = ManageClientThreads.getServerConnectClientThread(receiverId);

                            ObjectOutputStream oos = new ObjectOutputStream(scct.socket.getOutputStream());

                            oos.writeObject(msg);

                        }
                    }

                } else if (msg.getMsgType().equals(MessageType.MESSAGE_FILE_MESS)) {
                    String receiveId = msg.getReceiver();

                    ServerConnectClientThread scct = ManageClientThreads.getServerConnectClientThread(receiveId);

                    ObjectOutputStream oos = new ObjectOutputStream(scct.socket.getOutputStream());

                    oos.writeObject(msg);


                } else {
                    System.out.println("Other Msg Type");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
