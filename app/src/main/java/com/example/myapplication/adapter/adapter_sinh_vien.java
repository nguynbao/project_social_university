package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.entity.User;

import java.util.List;

public class adapter_sinh_vien extends RecyclerView.Adapter<adapter_sinh_vien.ViewHolder> {
    List<User> userList;

    public adapter_sinh_vien(List<User> userList) {
        this.userList = userList;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView etName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.name);
        }
    }

    @NonNull
    @Override
    public adapter_sinh_vien.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sinh_vien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_sinh_vien.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.etName.setText(user.getUsername());
        holder.etName.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), user.getEmail(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
