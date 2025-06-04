package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_groupSV;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.GvPostDao;
import com.example.myapplication.data.entity.GvPost;
import com.example.myapplication.ui.activity_post_gr_sv;

import java.util.ArrayList;
import java.util.List;

public class fragment_group_sv extends Fragment {
    RecyclerView recyclerView;
    adapter_groupSV adapter_groupSV;
    GvPostDao gvPostDao;
    AppCompatButton share;
    List<GvPost> gvPostList = new ArrayList<>();
    public fragment_group_sv() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_sv, container, false);
        share = view.findViewById(R.id.share);
        share.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), activity_post_gr_sv.class));
        });
        gvPostDao = AppDatabase.getDatabase(getContext()).gvPostDao();
        recyclerView = view.findViewById(R.id.recycler_groupSV);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter_groupSV = new adapter_groupSV(gvPostList);
        recyclerView.setAdapter(adapter_groupSV);
        gvPostDao.getAllPostsLiveData().observe(getViewLifecycleOwner(), gvPosts -> {
            adapter_groupSV.setData(gvPosts);
        });
        return view;
    }
}
