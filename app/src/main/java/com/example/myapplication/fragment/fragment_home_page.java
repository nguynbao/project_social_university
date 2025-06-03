package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_home_pages;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.PostDao;
import com.example.myapplication.data.entity.Post;
import com.example.myapplication.component.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class fragment_home_page extends Fragment {
    ImageView imgMenu;
    RecyclerView recyclerView;
    adapter_home_pages adapter;
    PostDao postDao;
    List<Post> postList ;
    public fragment_home_page() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        imgMenu = view.findViewById(R.id.menuhp);
        imgMenu.setOnClickListener(v -> {
            menu.openMenu(requireContext(), v);
        });
        recyclerView = view.findViewById(R.id.recycler_homepage);
        postList = new ArrayList<>();
        adapter = new adapter_home_pages(postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        Executors.newSingleThreadExecutor().execute(()->{
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            postDao = db.postDao();
            List<Post> posts = postDao.getAllPosts();
            requireActivity().runOnUiThread(() -> {
                postList.clear();
                postList.addAll(posts);
                adapter.notifyDataSetChanged();
            });
        });
        return view;
    }
}
