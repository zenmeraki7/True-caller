package com.example.caller;
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
import java.util.List;
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private final List<Contact> contacts;
    private Context ctx;

    public ContactsAdapter(Context ctx, List<Contact> contacts) {
        this.ctx = ctx;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        String contactName = !TextUtils.isEmpty(contact.getName()) ? contact.getName() : "Unknown";
        holder.textViewName.setText(contactName);

        // Set the first letter of the contact name or default to "U" for unknown
        holder.textViewLetter.setVisibility(View.VISIBLE);
        holder.textViewLetter.setText(contactName.substring(0, 1).toUpperCase());

        // Set contact number or display a default message
        String contactNumber = !TextUtils.isEmpty(contact.getPhoneNumber()) ? contact.getPhoneNumber() : "No number";
        holder.textViewNumber.setText(contactNumber);

        // Set call details (e.g., call type and timestamp)
        holder.textViewDetails.setText(contact.getDetails());

        // Call button click handler
        holder.buttonCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));




            // Start call activity
            ctx.startActivity(callIntent);
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewNumber, textViewDetails, textViewLetter;
        ImageButton buttonCall;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewNumber = itemView.findViewById(R.id.textViewNumber); // Add this TextView to display the number
            textViewDetails = itemView.findViewById(R.id.textViewDetails);
            buttonCall = itemView.findViewById(R.id.buttonCall);
            textViewLetter = itemView.findViewById(R.id.textViewLetter); // Add a TextView for the letter
        }
    }
}

