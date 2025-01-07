package com.example.caller.ui.callog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.caller.ContactListActivity;
import com.example.caller.R;
import com.example.caller.databinding.FragmentCallogBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallogFragment extends Fragment {

    private FragmentCallogBinding binding;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCallogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        checkPermissions();
        setupSearchBar();
        setupFloatingActionButton();

        Button btnContactList = root.findViewById(R.id.btnContactList);
        btnContactList.setOnClickListener(v -> {
            // Start the new activity
            Intent intent = new Intent(getActivity(), ContactListActivity.class);
            startActivity(intent);
        });

        return root;
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            loadCallLogs();
        }
    }

    private void loadCallLogs() {
        List<CallLogItem> callLogList = new ArrayList<>();
        Cursor cursor = requireContext().getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));

                String date = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(new Date(timestamp));

                String callType = "Unknown";
                int callTypeCode = Integer.parseInt(type);
                switch (callTypeCode) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "Incoming";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "Outgoing";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "Missed";
                        break;
                }

                callLogList.add(new CallLogItem(name != null ? name : number, number, callType + " - " + date));
            }
            cursor.close();
        }

        CallLogAdapter adapter = new CallLogAdapter(requireContext(), callLogList);
        binding.recyclerViewCallLog.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCallLog.setAdapter(adapter);
    }

    private void setupSearchBar() {
        EditText searchBar = binding.searchBar;
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the call logs when the user types in the search bar
                String query = s.toString().toLowerCase();
                if (binding.recyclerViewCallLog.getAdapter() instanceof CallLogAdapter) {
                    ((CallLogAdapter) binding.recyclerViewCallLog.getAdapter()).filter(query);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });
    }

    private void setupFloatingActionButton() {
        binding.fabCall.setOnClickListener(v -> {
            // Trigger the DialPadFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DialingPadFragment()) // Replace with your fragment container ID
                    .addToBackStack(null) // Optional: Adds the transaction to the back stack
                    .commit();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadCallLogs();
            } else {
                Toast.makeText(requireContext(), "Permission denied to read call logs", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
