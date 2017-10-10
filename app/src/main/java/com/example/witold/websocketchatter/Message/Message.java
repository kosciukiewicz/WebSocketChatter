package com.example.witold.websocketchatter.Message;

/**
 * Created by Witold on 2017-04-05.
 */

public class Message {
    private String client;
    private String content;
    private int type;

    public Message(String client, String content, int type)
    {
        this.client = client;
        this.content = content;
        this.type = type;
    }

    public String getClient() {
        return client;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
