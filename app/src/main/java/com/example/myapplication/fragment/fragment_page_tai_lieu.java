package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.component.menu;

public class fragment_page_tai_lieu extends Fragment {
    ImageView img_backdoc;

    public fragment_page_tai_lieu() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_tai_lieu, container, false);

        img_backdoc = view.findViewById(R.id.img_backdoc);
        img_backdoc.setOnClickListener(v -> {
            menu.openMenu(requireContext(), v);
        });
        return view;
    }

}
