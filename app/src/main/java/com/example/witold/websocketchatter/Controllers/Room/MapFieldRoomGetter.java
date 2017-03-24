package com.example.witold.websocketchatter.Controllers.Room;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Witold on 2017-03-24.
 */

public class MapFieldRoomGetter {
    public static Map<String, String> getMapFieldsForAddRoomToServer(String name, int capacity)
    {
        Map<String,String> fields = new HashMap<>();
        fields.put(RoomConstants.ROOM_NAME, name);
        fields.put(RoomConstants.MAX_CAPACITY, String.valueOf(capacity));
        return fields;
    }
}
