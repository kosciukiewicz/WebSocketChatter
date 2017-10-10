package com.example.witold.websocketchatter.Room;

import com.example.witold.websocketchatter.Room.RoomProvider.RoomConstants;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Witold on 2017-03-14.
 */

public class Room {
    @SerializedName(value = RoomConstants.ROOM_ID)
    private String id;
    @SerializedName(value = RoomConstants.ROOM_NAME)
    private String name;
    @SerializedName(value = RoomConstants.MAX_CAPACITY)
    private int maxCapacity;
    @SerializedName(value = RoomConstants.CLIENTS_COUNT)
    private int clientsCount;

    public Room(String id, String name, int maxCapacity, int clientsCount)
    {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.clientsCount= clientsCount;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getMaxCapacity()
    {
        return maxCapacity;
    }

    public int getClientsCount()
    {
        return clientsCount;
    }
}
