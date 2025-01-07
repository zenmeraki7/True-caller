package com.example.caller.ui.callog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.caller.R;

public class DialingPadFragment extends Fragment {

    private EditText numberDisplay;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialing_pad, container, false);

        numberDisplay = root.findViewById(R.id.number_display);
        setupDialpadButtons(root);

        ImageView callButton = root.findViewById(R.id.call_button);
        callButton.setOnClickListener(v -> makeCall());

        return root;
    }

    private void setupDialpadButtons(View root) {
        GridLayout dialpad = root.findViewById(R.id.dialpad_grid1);
        for (int i = 0; i < dialpad.getChildCount(); i++) {
            View child = dialpad.getChildAt(i);
            if (child instanceof Button) {
                child.setOnClickListener(v -> {
                    String text = ((Button) v).getText().toString();
                    if (text.equalsIgnoreCase("Clear")) {
                        numberDisplay.setText("");
                    } else {
                        numberDisplay.append(text);
                    }
                });
            }
        }
    }

    private void makeCall() {
        String phoneNumber = numberDisplay.getText().toString();
        if (!TextUtils.isEmpty(phoneNumber)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        }
    }
}