package com.philimonnag.chat.Model;

public class Message {
    String msgTxt,sender,timeStamp,userId;

    public Message(String msgTxt, String sender, String timeStamp, String userId) {
        this.msgTxt = msgTxt;
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.userId = userId;
    }

    public Message() {
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
