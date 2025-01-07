package com.example.caller.ui.blocked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.caller.databinding.FragmentBlockedBinding;

public class BlockedFragment extends Fragment {

    private FragmentBlockedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BlockedViewModel blockedViewModel =
                new ViewModelProvider(this).get(BlockedViewModel.class);

        binding = FragmentBlockedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textBlocked;
        blockedViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}