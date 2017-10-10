package com.example.witold.websocketchatter.ChatFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.witold.websocketchatter.Message.Message;
import com.example.witold.websocketchatter.Message.MessageConstants;
import com.example.witold.websocketchatter.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witold on 2017-04-05.
 */

public class MessageAdapter extends BaseAdapter {
    private List<Message> messageList;
    private LayoutInflater inflater;
    Context context;


    public MessageAdapter(Context activity, List<Message> messageList) {
        this.context = activity;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View newView;
        if(messageList.get(position).getType() == MessageConstants.JOIN_MESSAGE)
        {
            newView = inflater.inflate(R.layout.join_room_massage_layout, null);
            ((TextView)newView.findViewById(R.id.textViewJoinText)).setText(messageList.get(position).getClient() + " " + messageList.get(position).getContent());
        }
        else
        {
            newView = inflater.inflate(R.layout.single_message_layout, null);
            MessageAdapter.ViewHolder viewHolder = new MessageAdapter.ViewHolder(newView);
            if(!messageList.get(position - 1).getClient().equals(messageList.get(position).getClient()) || messageList.get(position - 1).getType() != messageList.get(position).getType()) {
                viewHolder.textViewClient.setText(messageList.get(position).getClient() + ": ");
            }
            else
            {
                viewHolder.textViewClient.setHeight(0);
            }
            viewHolder.textViewContent.setText(messageList.get(position).getContent());
            if(messageList.get(position).getType() == MessageConstants.OWN_MESSAGE)
            {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.textViewContent.getLayoutParams();
                params.removeRule(RelativeLayout.ALIGN_PARENT_START);
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                viewHolder.textViewContent.setLayoutParams(params);
                viewHolder.textViewContent.setBackground(context.getDrawable(R.drawable.own_message_background));
            }
        }
        return newView;
    }

    class ViewHolder
    {
        @BindView(R.id.textViewMessageClient)
        TextView textViewClient;
        @BindView(R.id.textViewMessageContent) TextView textViewContent;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
