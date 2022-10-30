package com.example.tinkoffapp;

public class Message {

    private String sender;
    private String receive;
    private String content;

    public Message() {
        sender = "";
        receive = "";
        content = "";
    }

    public Message(String sender, String receive, String content) {
        this.sender = sender;
        this.receive = receive;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}