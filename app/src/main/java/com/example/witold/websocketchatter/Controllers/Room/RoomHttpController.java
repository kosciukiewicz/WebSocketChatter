package com.example.witold.websocketchatter.Controllers.Room;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.example.witold.websocketchatter.Constants.ServerConstants;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Witold on 2017-03-14.
 */

public class RoomHttpController {
    Context context;

    public RoomHttpController(Context context)
    {
        this.context = context;
    }
    public void getRoomFromServer(String url, GetRoomFromServerCallback callback)
    {
        (new TryGetRoomListFromServer(url, callback)).execute();
    }

    public void AddNewRoomToServer(String url, String arguments, AdRoomToServerCallback callback)
    {
        (new TryAddRoomToServer(url, arguments, callback)).execute();
    }

    private void handleError()
    {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((HttpControllerHolder)context).onError();
            }
        });
    }

    private class TryGetRoomListFromServer extends AsyncTask<Void, Void, List<Room>> {

        GetRoomFromServerCallback callback;
        String request_url;
        public TryGetRoomListFromServer(String url, GetRoomFromServerCallback callback)
        {
            request_url = url;
            this.callback = callback;
        }
        @Override
        protected List<Room> doInBackground(Void... params) {
            return getListObjectFromJsonArray(getJsonArrayFromServer());
        }

        protected List<Room> getListObjectFromJsonArray(JSONArray jsonArray)
        {
            if(jsonArray != null)
            {
                List<Room> roomList = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    try {
                        Room room = getObjectFromJson(jsonArray.getJSONObject(i));
                        if(room != null)
                        {
                            roomList.add(room);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                return roomList;
            }
            return null;
        }

        private Room getObjectFromJson(JSONObject jsonObject)
        {
            try {
                return new Room(
                        jsonObject.getString(RoomConstants.ROOM_ID),
                        jsonObject.getString(RoomConstants.ROOM_NAME),
                        jsonObject.getInt(RoomConstants.MAX_CAPACITY),
                        jsonObject.getInt(RoomConstants.CLIENTS_COUNT)
                );
            }catch(Exception e) {
                handleError();
            }
            return null;
        }

        protected JSONArray getJsonArrayFromServer()
        {
            try {
                String urlComposer = "http://" + ServerConstants.URL + "/Rooms";
                URL url = new URL(urlComposer);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                return new JSONArray(IOUtils.toString(in, "UTF-8"));
            }catch (Exception e)
            {
                handleError();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Room> rooms) {
            callback.done(rooms);
            super.onPostExecute(rooms);
        }
    }

    private class TryAddRoomToServer extends AsyncTask<Void, Void, String> {

        AdRoomToServerCallback callback;
        String request_url;
        String postArguments;
        public TryAddRoomToServer(String url, String arguments, AdRoomToServerCallback callback)
        {
            request_url = url;
            this.postArguments = arguments;
            this.callback = callback;
        }
        @Override
        protected String doInBackground(Void... params) {
            return putRoomOnServer();
        }

        protected String putRoomOnServer()
        {
            try {
                String urlComposer = "http://" + ServerConstants.URL + "/Rooms";
                URL url = new URL(urlComposer);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(postArguments);
                out.close();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                return IOUtils.toString(in, "UTF-8");
            }catch (Exception e)
            {
                handleError();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            callback.done(string);
            super.onPostExecute(string);
        }
    }



    public interface HttpControllerHolder
    {
        public abstract void onError();
    }
}
