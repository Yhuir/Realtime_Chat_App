package com.qqserver.service;

import com.qqcommon.Message;
import com.qqcommon.MessageType;
import com.utility.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {

        System.out.print("Please enter news to send: ");
        String news = Utility.readString(50);

        // Create a new message to send to all clients
        Message message = new Message();
        message.setSender("System");
        message.setContent(news);
        message.setMsgType(MessageType.MESSAGE_NEWS);
        message.setSendTime(new Date().toString());

        System.out.println(message.getSender() + " send to everyone: " + message.getContent());

        // Get the list of all connected client threads
        HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
        Iterator<String> iterator = hm.keySet().iterator();

        // Loop through all clients and send the message
        while (iterator.hasNext()) {
            String receiverId = iterator.next();
            ServerConnectClientThread scct = hm.get(receiverId);

            try {
                // Assuming ServerConnectClientThread has a method to get the socket
                ObjectOutputStream oos = new ObjectOutputStream(scct.getSocket().getOutputStream());

                // Send the message to the client
                oos.writeObject(message);

            } catch (IOException e) {
                System.err.println("Error sending message to client " + receiverId);
                e.printStackTrace();
            }
        }
    }
}
