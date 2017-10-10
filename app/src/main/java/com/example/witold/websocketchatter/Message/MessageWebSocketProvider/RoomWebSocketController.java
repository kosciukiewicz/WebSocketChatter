package com.example.witold.websocketchatter.Message.MessageWebSocketProvider;

import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;

/**
 * Created by Witold on 2017-03-10.
 */
public class RoomWebSocketController {
    public static void JoinRoom(WebSocketConnection mConnection, JSONObject jsonObject)
    {
        mConnection.sendTextMessage(jsonObject.toString());
    }

    public static void SendMessage(WebSocketConnection mConnection, JSONObject jsonObject)
    {
        mConnection.sendTextMessage(jsonObject.toString());
    }
}
