package com.example.witold.websocketchatter.Controllers.Room;

/**
 * Created by Witold on 2017-03-14.
 */

public class Room {
    private String id;
    private String name;
    private int maxCapacity;
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
