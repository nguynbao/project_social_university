package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.List;
import java.util.concurrent.Executors;

public class fragment_home_page extends Fragment {
    public fragment_home_page() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        RecyclerView recycler_homepage = view.findViewById(R.id.recycler_homepage);
        recycler_homepage.setLayoutManager(new LinearLayoutManager(getContext()));
        Executors.newSingleThreadExecutor().execute(()->{
            AppDatabase db = AppDatabase.getDatabase(getContext());
            PostDao postDao = db.postDao();
            List<Post> postList = postDao.getAllPosts();
            for (Post post : postList){
                Log.d("Post:", post.getNameClub());
            }
            adapter_home_pages adapterHomePages = new adapter_home_pages(postList);
            recycler_homepage.setAdapter(adapterHomePages);
        });
        return view;
    }
}