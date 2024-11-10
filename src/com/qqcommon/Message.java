package com.qqcommon;

import java.io.Serializable;

public class Message implements Serializable {

    private String sender;
    private String receiver;
    private String content;
    private String sendTime;
    private String msgType;

    public Message() {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.msgType = msgType;
        this.sendTime = sendTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
