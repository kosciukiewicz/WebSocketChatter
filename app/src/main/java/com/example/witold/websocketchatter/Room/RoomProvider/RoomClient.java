package com.example.witold.websocketchatter.Room.RoomProvider;

import com.example.witold.websocketchatter.Room.Room;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Witold on 2017-03-22.
 */

public interface RoomClient {
    @GET("/Rooms")
    Call<List<Room>> roomsFromServer();

    @FormUrlEncoded
    @POST("/Rooms")
    //Call<ResponseBody> addRoomTOServer(@Field(RoomConstants.ROOM_NAME) String roomName, @Field(RoomConstants.MAX_CAPACITY) String maxCapacity);
    Call<ResponseBody> addRoomTOServer(@FieldMap Map<String,String> fields);

}
