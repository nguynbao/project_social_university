package com.example.myapplication.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.entity.Document;
import com.example.myapplication.ui_admin.admin_editDoc;

import java.util.List;

public class adapter_tai_lieu extends RecyclerView.Adapter<adapter_tai_lieu.ViewHolder>{
    List<Document> documentList;
    public adapter_tai_lieu(List<Document> documentList) {
        this.documentList = documentList;
    }
    public void setData(List<Document> documentList) {
        this.documentList = documentList;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView etName;
        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.nameTL);
        }
    }

    @NonNull
    @Override
    public adapter_tai_lieu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = View.inflate(parent.getContext(), R.layout.item_tai_lieu, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_tai_lieu.ViewHolder holder, int position) {
        Document document = documentList.get(position);
        holder.etName.setText(document.getTitle());
        holder.etName.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), admin_editDoc.class);
            intent.putExtra("documentID", document.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }
}
