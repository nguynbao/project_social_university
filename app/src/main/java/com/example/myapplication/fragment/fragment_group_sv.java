package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_groupSV;
import com.example.myapplication.component.menu;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.GvPostDao;
import com.example.myapplication.data.dao.LikeDao;
import com.example.myapplication.data.entity.GvPost;
import com.example.myapplication.data.entity.Like;
import com.example.myapplication.ui.activity_post_gr_sv;
import com.example.myapplication.component.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class fragment_group_sv extends Fragment {
    RecyclerView recyclerView;
    adapter_groupSV adapter_groupSV;
    GvPostDao gvPostDao;
    LikeDao likeDao;
    ImageView img_backGrsv;

    AppCompatButton share;
    List<GvPost> gvPostList = new ArrayList<>();
    public fragment_group_sv() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_sv, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int studentId = sharedPreferences.getInt("student_id", -1);
        share = view.findViewById(R.id.share);
        share.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), activity_post_gr_sv.class));
        });
        img_backGrsv = view.findViewById(R.id.menuhp_group);
        img_backGrsv.setOnClickListener(v -> {
            menu.openMenu(requireContext(), v);
        });
        gvPostDao = AppDatabase.getDatabase(getContext()).gvPostDao();
        likeDao = AppDatabase.getDatabase(getContext()).likeDao();
        recyclerView = view.findViewById(R.id.recycler_groupSV);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter_groupSV = new adapter_groupSV(
                gvPostList,
                (post, position) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        Like existingLike = likeDao.getUserLikeForPost(post.getId(), studentId);
                        if (existingLike != null) {
                            likeDao.delete(existingLike);
                        } else {
                            likeDao.insert(new Like(post.getId(), studentId));
                        }
                        // Notify adapter
                        requireActivity().runOnUiThread(() -> {
                            adapter_groupSV.notifyItemChanged(position);
                        });
                    });
                },
                likeDao,
                studentId
        );
        recyclerView.setAdapter(adapter_groupSV);
        gvPostDao.getAllPostsLiveData().observe(getViewLifecycleOwner(), gvPosts -> {
            adapter_groupSV.setData(gvPosts);
        });
        ImageView menuhp_group = view.findViewById(R.id.menuhp_group);
        menuhp_group.setOnClickListener(view1 -> {
            menu.openMenu(getContext(), view1);
        });
        return view;
    }
}
