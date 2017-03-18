package com.example.witold.websocketchatter.Constants;

import android.util.Log;

import com.example.witold.websocketchatter.Controllers.Room.RoomConstants;

/**
 * Created by Witold on 2017-03-17.
 */

public class ArgumentStringGetter {
    public static String POST_ARGUMENT_SUTBSTRING = "=";
    public static String getArgumentsForAddRoomToServer(String roomName, int capacity)
    {
        StringBuilder arguments = new StringBuilder("");
        Log.d("asdsdf", arguments.append(RoomConstants.ROOM_NAME)
                .append(POST_ARGUMENT_SUTBSTRING)
                .append(roomName).append("&")
                .append(RoomConstants.MAX_CAPACITY)
                .append(POST_ARGUMENT_SUTBSTRING)
                .append(capacity).toString());
        return arguments.toString();
    }
}
