package com.example.witold.websocketchatter.ViewControllers;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.witold.websocketchatter.Controllers.Room.GetRoomFromServerCallback;
import com.example.witold.websocketchatter.Controllers.Room.Room;
import com.example.witold.websocketchatter.Controllers.Room.RoomHttpController;
import com.example.witold.websocketchatter.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.witold.websocketchatter.R.id.swipeContainer;

public class JoinRoomFragments extends Fragment {
    @BindView(R.id.listViewRooms) ListView listViewRooms;
    @BindView(R.id.floatingActionButtonAddRoom) FloatingActionButton floatingActionButton;
    @BindView(swipeContainer) SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.floatingActionButtonAddRoom)
    public void showAddRoomDialog()
    {
        DialogFragment newFragment =  new AddRoomDialog();
        newFragment.show(getActivity().getSupportFragmentManager() ,"");
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

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setRefreshing(true);
        getRoomListFromServerAndSetAdapter();
    }

    private void getRoomListFromServerAndSetAdapter()
    {
        new RoomHttpController(getContext()).getRoomFromServer("", new GetRoomFromServerCallback() {
            @Override
            public void done(List<Room> rooms) {
                if(rooms!=null) {
                    setListViewRoomsAdapter(rooms);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setListViewRoomsAdapter(List<Room> rooms)
    {
        RoomAdapter adapter = new RoomAdapter(getContext(), rooms, this);
        listViewRooms.setAdapter(adapter);
    }

    public void showNickNameDialoToLoginToChosenRoom(String roomID) // method called by RoomAdapter
    {
        Bundle bundle = new Bundle();
        bundle.putString("roomId", roomID);
        DialogFragment newFragment =  new NickNameDialog();
        newFragment.setArguments(bundle);
        newFragment.show(getActivity().getSupportFragmentManager() ,"");
        //connectionHandler.startNicknameDialog(roomID); //connecting to room method in main activity
    }


}
