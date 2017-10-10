package com.example.witold.websocketchatter.ChatFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.witold.websocketchatter.Message.Message;
import com.example.witold.websocketchatter.Message.MessageConstants;
import com.example.witold.websocketchatter.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatFragment extends Fragment {
    @BindView(R.id.editTextWriteMessage)
    EditText editTextChat;
    @BindView(R.id.buttonSend)
    Button buttonSend;
    MessageSendHandler mHandler;
    MessageAdapter adapter;
    List<Message> messageList;
    @BindView(R.id.listViewMessages)
    ListView listViewMessages;
    private String nick;

    @OnClick(R.id.buttonSend)
    public void sendMessage() {
        if (!editTextChat.getText().toString().equals("")) {
            mHandler.sendMessage(editTextChat.getText().toString());
            editTextChat.setText("");
        }
    }

    public interface MessageSendHandler {
        public void sendMessage(String message);

        public void closeCOnnection();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHandler = (MessageSendHandler) context;
    }

    @Override
    public void onDetach() {
        mHandler.closeCOnnection();
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.closeCOnnection();
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        ButterKnife.bind(this, view);
        messageList = new ArrayList<>();
        adapter = new MessageAdapter(getContext(), messageList);
        listViewMessages.setAdapter(adapter);
        return view;
    }

    public void receiveMessage(String nick, String message) {
        if (nick.equals(this.nick)) {
            messageList.add(new Message(nick, message, MessageConstants.OWN_MESSAGE));
        } else {
            messageList.add(new Message(nick, message, MessageConstants.TEXT_MESSAGE));
        }
        adapter.notifyDataSetChanged();
    }

    public void receiveJoinMessage(String nick, String message) {
        messageList.add(new Message(nick, message, MessageConstants.JOIN_MESSAGE));
        adapter.notifyDataSetChanged();
    }

}
