package com.example.caller.ui.premium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.caller.databinding.FragmentPremiumBinding;

public class PremiumFragment extends Fragment {

    private FragmentPremiumBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PremiumViewModel messagesViewModel =
                new ViewModelProvider(this).get(PremiumViewModel.class);

        binding = FragmentPremiumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPremium;
        messagesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}