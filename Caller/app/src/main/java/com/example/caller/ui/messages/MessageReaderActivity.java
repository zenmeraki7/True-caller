package com.example.caller.ui.messages;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caller.R;

public class MessageReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_reader);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Find views by ID
        TextView senderName = findViewById(R.id.textViewSender);
        TextView messageContent = findViewById(R.id.textViewMessageContent);
        TextView messageTime = findViewById(R.id.textViewMessageTime);
        Button backButton = findViewById(R.id.buttonBack);

        // Get intent extras
        String sender = getIntent().getStringExtra("sender");
        String content = getIntent().getStringExtra("content");
        String time = getIntent().getStringExtra("time");

        // Set values
        senderName.setText(sender != null ? sender : "Unknown");
        messageContent.setText(content != null ? content : "No message content available.");
        messageTime.setText(time != null ? time : "Unknown time");

        // Back button listener
        backButton.setOnClickListener(v -> finish());
    }
}
