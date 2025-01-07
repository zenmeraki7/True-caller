package com.example.caller.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caller.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messagesList;
    private Context context;

    public MessageAdapter(Context context, List<Message> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messagesList.get(position);

        holder.senderName.setText(message.getSenderName());
        holder.messageContent.setText(message.getMessage());
        holder.messageTime.setText(message.getTime());

        if (message.getSenderName() != null && !message.getSenderName().isEmpty()) {
            String firstLetter = message.getSenderName().substring(0, 1).toUpperCase();
            holder.senderImage.setText(firstLetter);
            holder.senderImage.setVisibility(View.VISIBLE);
        } else {
            holder.senderImage.setVisibility(View.GONE);
        }

        // Click listener for opening the message reader
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageReaderActivity.class);
            intent.putExtra("sender", message.getSenderName());
            intent.putExtra("content", message.getMessage());
            intent.putExtra("time", message.getTime());
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView senderName, messageContent, messageTime, senderImage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.textViewSenderName);
            messageContent = itemView.findViewById(R.id.textViewMessageContent);
            messageTime = itemView.findViewById(R.id.textViewMessageTime);
            senderImage = itemView.findViewById(R.id.textViewSenderImage);
        }
    }
}

