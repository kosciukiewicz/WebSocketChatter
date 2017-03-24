package com.example.witold.websocketchatter.ViewControllers;

import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.witold.websocketchatter.Constants.ServerConstants;
import com.example.witold.websocketchatter.Controllers.Room.HttpControllerHolder;
import com.example.witold.websocketchatter.Controllers.Room.Room;
import com.example.witold.websocketchatter.Controllers.Room.RoomClient;
import com.example.witold.websocketchatter.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.witold.websocketchatter.R.id.swipeContainer;

public class JoinRoomFragments extends Fragment {
    @BindView(R.id.listViewRooms)
    ListView listViewRooms;
    @BindView(R.id.floatingActionButtonAddRoom)
    FloatingActionButton floatingActionButton;
    @BindView(swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    HttpControllerHolder httpControllerHolder;
    Retrofit retrofit;
    RoomClient roomClient;

    @OnClick(R.id.floatingActionButtonAddRoom)
    public void showAddRoomDialog() {
        DialogFragment newFragment = new AddRoomDialog();
        newFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        httpControllerHolder = (HttpControllerHolder) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_room_fragments, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRoomListFromServerAndSetAdapter();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary);
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(ServerConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();
        roomClient = retrofit.create(RoomClient.class);
        swipeRefreshLayout.setRefreshing(true);
        getRoomListFromServerAndSetAdapter();
    }

    private void getRoomListFromServerAndSetAdapter() {
        Call<List<Room>> call = roomClient.roomsFromServer();

        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                List<Room> rooms = response.body();
                setListViewRoomsAdapter(rooms);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                httpControllerHolder.onError();
                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void setListViewRoomsAdapter(List<Room> rooms) {
        RoomAdapter adapter = new RoomAdapter(getContext(), rooms, this);
        listViewRooms.setAdapter(adapter);
    }

    public void showNickNameDialoToLoginToChosenRoom(String roomID) // method called by RoomAdapter
    {
        Bundle bundle = new Bundle();
        bundle.putString("roomId", roomID);
        DialogFragment newFragment = new NickNameDialog();
        newFragment.setArguments(bundle);
        newFragment.show(getActivity().getSupportFragmentManager(), "");
        //connectionHandler.startNicknameDialog(roomID); //connecting to room method in main activity
    }

}
