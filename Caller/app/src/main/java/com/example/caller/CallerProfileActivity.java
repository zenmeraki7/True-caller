package com.example.caller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caller.ui.callog.CallLogItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CallerProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewNumber, textViewEmail,textViewLetter;
    private RecyclerView recyclerViewCallHistory;
    private com.example.caller.CallLogHisAdapter callLogHisAdapter;
    private List<CallLogHisItem> callLogHisList = new ArrayList<>();
    private Button buttonShowMore;
    ImageView buttoncall;

    private static final int INITIAL_CALL_LOG_LIMIT = 5;
    private int currentLimit = INITIAL_CALL_LOG_LIMIT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caller_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        textViewName = findViewById(R.id.textViewName);
        textViewNumber = findViewById(R.id.textViewNumber);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewLetter = findViewById(R.id.textViewLetter);
        recyclerViewCallHistory = findViewById(R.id.recyclerViewCallHistory);
        buttonShowMore = findViewById(R.id.buttonShowMore);
        buttoncall = findViewById(R.id.buttonCall);

        recyclerViewCallHistory.setLayoutManager(new LinearLayoutManager(this));
        callLogHisAdapter = new CallLogHisAdapter(this, callLogHisList);
        recyclerViewCallHistory.setAdapter(callLogHisAdapter);

        // Get extras from intent
        String callerName = getIntent().getStringExtra("callerName");
        String callerNumber = getIntent().getStringExtra("callerNumber");
        String callerEmail = getIntent().getStringExtra("callerEmail");
        String callerLetter = getIntent().getStringExtra("callerLetter");
        String callerNetwork = getIntent().getStringExtra("callerNetwork");

        textViewName.setText(!TextUtils.isEmpty(callerName) ? callerName : "Unknown");
        textViewNumber.setText(!TextUtils.isEmpty(callerNumber) ? callerNumber : "No number");
        textViewEmail.setText(!TextUtils.isEmpty(callerEmail) ? callerEmail : "No email");
        textViewLetter.setText(!TextUtils.isEmpty(callerLetter) ? callerLetter : "?");

        loadCallHistory(this, callerNumber);


        buttonShowMore.setOnClickListener(v -> {
            // Load the entire call log
            currentLimit = Integer.MAX_VALUE; // Or any sufficiently large value
            loadCallHistory(this, callerNumber);
        });
        buttoncall.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(callerNumber)) {
                makeCall(callerNumber);
            } else {
                Toast.makeText(this, "No valid number to call", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeCall(String phoneNumber) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);


    }


    private void loadCallHistory(Context context, String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI,
                    null,
                    CallLog.Calls.NUMBER + " = ?",
                    new String[]{phoneNumber},
                    CallLog.Calls.DATE + " DESC"
            );

            if (cursor != null) {
                int count = 0;
                while (cursor.moveToNext() && count < currentLimit) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                    String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    long dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    String type = getCallType(cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE)));

                    // Format the call date
                    String formattedDate = formatDate(dateMillis);

                    // Add to list if not already present
                    if (count >= callLogHisList.size()) {
                        callLogHisList.add(new CallLogHisItem(name, number, type + " on " + formattedDate));
                    }
                    count++;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        callLogHisAdapter.notifyDataSetChanged();

        // Show/Hide Show More button
        buttonShowMore.setVisibility(callLogHisList.size() < currentLimit ? View.GONE : View.VISIBLE);
    }

    private String getCallType(int type) {
        switch (type) {
            case CallLog.Calls.INCOMING_TYPE:
                return "Incoming";
            case CallLog.Calls.OUTGOING_TYPE:
                return "Outgoing";
            case CallLog.Calls.MISSED_TYPE:
                return "Missed";
            case CallLog.Calls.VOICEMAIL_TYPE:
                return "Voicemail";
            default:
                return "Unknown";
        }
    }

    private String formatDate(long dateMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        return dateFormat.format(new Date(dateMillis));
    }
}
