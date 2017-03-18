package com.example.witold.websocketchatter.ViewControllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.witold.websocketchatter.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoomFragment extends Fragment {
    @BindView(R.id.editTextWriteMessage) EditText editTextChat;
    @BindView(R.id.textViewChat) TextView textViewChat;
    @BindView(R.id.buttonSend) Button buttonSend;
    MessageSendHandler mHandler;

    @OnClick(R.id.buttonSend)
    public void sendMessage()
    {
        mHandler.sendMessage(editTextChat.getText().toString());
        editTextChat.setText("");
    }

    public interface MessageSendHandler
    {
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

    public RoomFragment() {
        // Required empty public constructor
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
        return view;
    }

    public void receiveMessage(String nick, String message)
    {
        String str = textViewChat.getText().toString();
        str += nick + ": " + message + "\n";
        textViewChat.setText(str);
    }

}
