package com.example.witold.websocketchatter.Controllers.Room;

import java.util.List;

/**
 * Created by Witold on 2017-03-14.
 */

public interface GetRoomFromServerCallback {
    public abstract void done(List<Room> rooms);
}
