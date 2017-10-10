package com.example.witold.websocketchatter.MainActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.witold.websocketchatter.ChatFragment.ChatFragment;
import com.example.witold.websocketchatter.Constants.JsonAttributesConstants;
import com.example.witold.websocketchatter.Constants.MessageTypeConstants;
import com.example.witold.websocketchatter.Constants.ServerConstants;
import com.example.witold.websocketchatter.Message.JsonGetter;
import com.example.witold.websocketchatter.Room.RoomProvider.HttpControllerHolder;
import com.example.witold.websocketchatter.Room.RoomProvider.MapFieldRoomGetter;
import com.example.witold.websocketchatter.Room.RoomProvider.RoomClient;
import com.example.witold.websocketchatter.Message.MessageWebSocketProvider.RoomWebSocketController;
import com.example.witold.websocketchatter.R;
import com.example.witold.websocketchatter.JoinRoomFragment.AddRoomDialog;
import com.example.witold.websocketchatter.JoinRoomFragment.JoinRoomFragment;
import com.example.witold.websocketchatter.JoinRoomFragment.ChooseNicknameDialog;
import com.example.witold.websocketchatter.Message.MessageWebSocketProvider.WebSocketConnectionCallback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Stack;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements AddRoomDialog.AddRoomDialogHolder, ChooseNicknameDialog.NicknameDialogHolder, ChatFragment.MessageSendHandler, HttpControllerHolder {
    private FragmentTransaction fragmentTransaction;
    private Stack<Fragment> fragments;
    private JoinRoomFragment joinRoomFragment;
    private ChatFragment chatFragment;
    private WebSocketConnection mConnection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragments();
        attachFragmentAndAddToStack(joinRoomFragment);
        initializeComponents();
    }

    private void addFragments()
    {
        initializeFragments();
        fragments = new Stack<>();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, joinRoomFragment);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, chatFragment);
    }

    private void detachAll()
    {
        fragmentTransaction.detach(joinRoomFragment);
        fragmentTransaction.detach(chatFragment);
    }

    private void checkIfStackContainsFragmentAndRemoveIt(Fragment fragment) //Żeby fragmenty nie powtarzały się na stosie;
    {
        if (fragments.contains(fragment)) {
            fragments.remove(fragment);
        }
    }

    private void attachFragmentAndAddToStack(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        detachAll();
        checkIfStackContainsFragmentAndRemoveIt(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
        fragments.add(fragment);
    }

    private void attachChatFragment(String nickname) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        detachAll();
        fragmentTransaction.attach(chatFragment);
        fragmentTransaction.commit();
        chatFragment.setNick(nickname);
    }

    private void attachLastFragmentFromStack() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        detachAll();
        fragments.pop();
        fragmentTransaction.attach(fragments.peek());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(fragments.size() > 1) // for now it's not necessary
        {
            attachLastFragmentFromStack();
        }
        if(chatFragment.isVisible())
        {
            attachFragmentAndAddToStack(joinRoomFragment);
        }
        else {
            super.onBackPressed();
        }
    }

    private void initializeFragments()
    {
        joinRoomFragment = new JoinRoomFragment();
        chatFragment = new ChatFragment();
    }

    private void initializeComponents()
    {
        if(mConnection==null) {
            mConnection = new WebSocketConnection();
        }
    }

    private void start(final WebSocketConnectionCallback callback) {

        final String wsuri = "ws://" + ServerConstants.URL + "/";

        try {
            mConnection.connect(wsuri, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    callback.done();
                }

                @Override
                public void onTextMessage(String payload) {
                    try {
                        JSONObject jsonObject = new JSONObject(payload);
                        String type = jsonObject.getString(JsonAttributesConstants.TYPE);
                        HandleMessage(type, jsonObject);
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        onError();
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d("WebSocket: ", "code: " + code + ", " + reason);
                    if (code != 3)
                    {
                        onError();
                        attachFragmentAndAddToStack(joinRoomFragment);
                    }
                }


            });
        } catch (WebSocketException e) {

            e.printStackTrace();
        }
    }

    private void HandleMessage(String type, JSONObject jsonObject)
    {
        switch (type)
        {
            case MessageTypeConstants.TEXT:
            {
                try {
                    chatFragment.receiveMessage(jsonObject.getString(JsonAttributesConstants.NICKNAME), jsonObject.getString(JsonAttributesConstants.TEXT));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            break;
            case MessageTypeConstants.JOIN_TEXT:
            {
                try {
                    chatFragment.receiveJoinMessage(jsonObject.getString(JsonAttributesConstants.NICKNAME), jsonObject.getString(JsonAttributesConstants.TEXT));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            break;
            case MessageTypeConstants.NO_SPACE:
            {
                Toast.makeText(getApplicationContext(), "No space in that room", Toast.LENGTH_SHORT).show();
                closeCOnnection();
                attachFragmentAndAddToStack(joinRoomFragment);
            }
        }
    }

    @Override
    public void connectToRoomWithNickname(final String roomId, final String nick) {
        if(!mConnection.isConnected()) {
            start(new WebSocketConnectionCallback() {
                @Override
                public void done() {
                    RoomWebSocketController.JoinRoom(mConnection,JsonGetter.getJsonObjectForJoinRoom(roomId, nick));
                }
            });
        }
        else
        {
            RoomWebSocketController.JoinRoom(mConnection,JsonGetter.getJsonObjectForJoinRoom(roomId, nick));
        }
        attachChatFragment(nick);
        }

    @Override
    public void sendMessage(String message) {
        try {
            RoomWebSocketController.SendMessage(mConnection, JsonGetter.getJsonObjectForSendMessageToRoommates(message));
        }catch (Exception e)
        {
            onError();
        }
    }

    @Override
    public void closeCOnnection() {
        mConnection.disconnect();
        Log.d("SSSS", "" + mConnection.isConnected());
    }

    public void addRoomToServer(String roomName, int capacity)
    {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(ServerConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).addConverterFactory(ScalarsConverterFactory.create());

        Retrofit retrofit = builder.build();
        RoomClient roomClient = retrofit.create(RoomClient.class);
        Call<ResponseBody> call = roomClient.addRoomTOServer(MapFieldRoomGetter.getMapFieldsForAddRoomToServer(roomName, capacity));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(getApplicationContext(), "" + response.body().string(), Toast.LENGTH_LONG).show();
                }catch(IOException e)
                {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onError();
                t.printStackTrace();
            }
        });
    }

    public void onError()
    {
        AlertDialog.Builder errorDialog =  new AlertDialog.Builder(this);
        errorDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        errorDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        errorDialog.setMessage("Server connection error");
        errorDialog.show();
    }
}
