package com.example.caller.ui.messages;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caller.R;
import com.example.caller.databinding.FragmentMessagesBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import androidx.core.app.ActivityCompat;

public class MessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private TextView textViewEmpty;

    private static final int PERMISSIONS_REQUEST_READ_SMS = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewMessages);
        textViewEmpty = root.findViewById(R.id.textViewEmpty);

        // Check permissions before loading messages
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, PERMISSIONS_REQUEST_READ_SMS);
        } else {
            loadMessages();
        }

        return root;
    }

    private void loadMessages() {
        List<Message> messagesList = new ArrayList<>();

        // Query SMS inbox
        Uri uri = Telephony.Sms.Inbox.CONTENT_URI;
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int addressIndex = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
            int bodyIndex = cursor.getColumnIndex(Telephony.Sms.BODY);
            int dateIndex = cursor.getColumnIndex(Telephony.Sms.DATE);

            do {
                String sender = cursor.getString(addressIndex);
                String message = cursor.getString(bodyIndex);
                String date = cursor.getString(dateIndex);

                // Convert timestamp to readable time format
                String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(Long.parseLong(date)));

                // Add message to list
                messagesList.add(new Message(sender, message, time));
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Show empty message or RecyclerView based on data availability
        if (messagesList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE); // Show "No messages found"
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE); // Hide empty state message

            // Set the adapter for RecyclerView
            messageAdapter = new MessageAdapter(getContext(), messagesList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(messageAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_SMS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadMessages();
        }
    }
}
