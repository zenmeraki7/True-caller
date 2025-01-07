package com.example.caller.ui.callog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caller.CallerProfileActivity;
import com.example.caller.R;

import java.util.ArrayList;
import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private final Context context;
    private final List<CallLogItem> callLogList;
    private final List<CallLogItem> fullList; // For storing original data for filtering

    public CallLogAdapter(Context context, List<CallLogItem> callLogList) {
        this.context = context;
        this.callLogList = new ArrayList<>(callLogList);
        this.fullList = new ArrayList<>(callLogList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallLogItem item = callLogList.get(position);

        String contactName = !TextUtils.isEmpty(item.getName()) ? item.getName() : "Unknown";
        holder.textViewName.setText(contactName);

        holder.textViewLetter.setText(contactName.substring(0, 1).toUpperCase());

        String contactNumber = !TextUtils.isEmpty(item.getNumber()) ? item.getNumber() : "No number";
        holder.textViewNumber.setText(contactNumber);

        holder.textViewDetails.setText(item.getDetails());

        holder.buttonCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + item.getNumber()));

            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                return;
            }

            context.startActivity(callIntent);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CallerProfileActivity.class);
            intent.putExtra("callerName", item.getName());
            intent.putExtra("callerNumber", item.getNumber());
            intent.putExtra("callerLetter", contactName.substring(0, 1).toUpperCase());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return callLogList.size();
    }

    public void filter(String query) {
        callLogList.clear();
        if (query.isEmpty()) {
            callLogList.addAll(fullList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (CallLogItem item : fullList) {
                if (item.getName().toLowerCase().contains(lowerCaseQuery) ||
                        item.getNumber().toLowerCase().contains(lowerCaseQuery)) {
                    callLogList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewNumber, textViewDetails, textViewLetter;
        ImageButton buttonCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewDetails = itemView.findViewById(R.id.textViewDetails);
            textViewLetter = itemView.findViewById(R.id.textViewLetter);
            buttonCall = itemView.findViewById(R.id.buttonCall);
        }
    }
}

