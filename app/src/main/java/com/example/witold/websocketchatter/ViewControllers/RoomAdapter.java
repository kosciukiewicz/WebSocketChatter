package com.example.witold.websocketchatter.ViewControllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.witold.websocketchatter.Controllers.Room.Room;
import com.example.witold.websocketchatter.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witold on 2017-03-14.
 */

public class RoomAdapter extends BaseAdapter {
    private List<Room> roomList;
    private LayoutInflater inflater;
    Context context;
    JoinRoomFragments fragments;


    public RoomAdapter(Context activity, List<Room> roomList, JoinRoomFragments fragment) {
        this.context = activity;
        this.roomList = roomList;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragments = fragment;
    }



    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int position) {
        return roomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View newView = inflater.inflate(R.layout.single_room_list_item_layout, null);
        ViewHolder viewHolder = new ViewHolder(newView);
        viewHolder.textViewRoomName.setText(roomList.get(position).getName());
        viewHolder.textViewRoomProperty.setText(roomList.get(position).getClientsCount() + "/" + roomList.get(position).getMaxCapacity());
        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragments.showNickNameDialoToLoginToChosenRoom(roomList.get(position).getId());
            }
        });
        return newView;
    }

     class ViewHolder
    {
        @BindView(R.id.textViewRoomName) TextView textViewRoomName;
        @BindView(R.id.textViewRoomProperty) TextView textViewRoomProperty;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
