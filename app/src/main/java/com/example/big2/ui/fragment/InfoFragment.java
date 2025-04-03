package com.example.big2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.big2.R;

public class InfoFragment extends Fragment {

    ImageView ivRuleHands, ivClose;
    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        ivRuleHands = view.findViewById(R.id.ivRuleHands);
        ivClose = view.findViewById(R.id.ivClose);

        // Close fragment when clicking outside the ImageView
        view.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Prevent closing when clicking on the image
        ivRuleHands.setOnClickListener(v -> {
            // Do nothing to consume the click
        });

        // Handle close button click
        ivClose.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }
}