package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.NotifyDao;
import com.example.myapplication.data.entity.Notify;

import java.util.List;

public class adapter_noti extends RecyclerView.Adapter<adapter_noti.ViewHolder> {
    List<Notify> notifyList;

    public adapter_noti(List<Notify> notifyList) {
        this.notifyList = notifyList;
    }

    public void setData(List<Notify> data) {
        this.notifyList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public adapter_noti.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noti, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_noti.ViewHolder holder, int position) {
        Notify notify = notifyList.get(position);
        holder.tvTitle.setText(notify.getTitle());
        holder.tvContent.setText(notify.getContent());
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            tvContent = itemView.findViewById(R.id.content);
        }
    }
}
