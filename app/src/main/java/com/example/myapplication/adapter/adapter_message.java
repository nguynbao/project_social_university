package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.entity.Message;

import java.util.List;

public class adapter_message extends RecyclerView.Adapter<adapter_message.ViewHolder> {
    List<Message> messages;
    Context context;

    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;

    private int CURRENT_USER_ID;

    public adapter_message(Context context, List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.context = context;
        this.CURRENT_USER_ID = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.getSenderId() == CURRENT_USER_ID ? TYPE_SENT : TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public adapter_message.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_send, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_message.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).txtMessage.setText(message.getContent());
        } else if (holder instanceof ReceivedViewHolder) {
            ((ReceivedViewHolder) holder).txtMessage.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class SentViewHolder extends adapter_message.ViewHolder {
        TextView txtMessage;

        SentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.sendMess);
        }
    }

    static class ReceivedViewHolder extends adapter_message.ViewHolder {
        TextView txtMessage;
        ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.receivedMess);
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
