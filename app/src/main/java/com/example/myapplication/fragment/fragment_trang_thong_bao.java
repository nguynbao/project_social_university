package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.adapter.adapter_noti;
import com.example.myapplication.data.dao.NotifyDao;
import com.example.myapplication.data.entity.Notify;

import java.util.ArrayList;
import java.util.List;

public class fragment_trang_thong_bao extends Fragment {
    RecyclerView recyclerView;
    adapter_noti adapter;
    NotifyDao notifyDao;
    List<Notify> notifyList = new ArrayList<>();
    public fragment_trang_thong_bao() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_thong_bao, container, false);
        recyclerView = view.findViewById(R.id.recycler_noti);
        Log.d("Fragment", "onCreateView called");
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new adapter_noti(notifyList);
        recyclerView.setAdapter(adapter);
        notifyDao = AppDatabase.getDatabase(requireContext()).notifyDao();
        notifyDao.getAllNotifies().observe(getViewLifecycleOwner(), notifies -> {
            adapter.setData(notifies);
            Log.d("Fragment", "Data updated: " + notifies.size());
        });
        return view;
    }
}
