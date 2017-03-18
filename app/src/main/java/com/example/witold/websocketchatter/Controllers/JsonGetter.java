package com.example.witold.websocketchatter.Controllers;

import com.example.witold.websocketchatter.Constants.JsonAttributesConstants;
import com.example.witold.websocketchatter.Constants.MessageTypeConstants;

import org.json.JSONObject;

/**
 * Created by Witold on 2017-03-10.
 */
public class JsonGetter {
    public static JSONObject getJsonObjectForJoinRoom(String serverId, String nickname)
    {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(JsonAttributesConstants.TYPE, MessageTypeConstants.JOIN);
            jsonObject.accumulate(JsonAttributesConstants.ROOM_ID, serverId);
            jsonObject.accumulate(JsonAttributesConstants.NICKNAME, nickname);
            return jsonObject;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJsonObjectForSendMessageToRoommates(String message)
    {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(JsonAttributesConstants.TYPE, MessageTypeConstants.TEXT);
            jsonObject.accumulate(JsonAttributesConstants.TEXT, message);
            return jsonObject;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


}
