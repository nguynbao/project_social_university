package com.example.myapplication.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.entity.Document;

import java.util.List;

public class adapter_document extends RecyclerView.Adapter<adapter_document.ViewHolder> {
    List<Document> documentList;
    String url_Join;
    Context context;
    public adapter_document(List<Document> documentList, Context context) {
        this.documentList = documentList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameClub_doc, content_Doc;
        Button btnJoin;
        public ViewHolder(View itemView) {
            super(itemView);
            nameClub_doc = itemView.findViewById(R.id.nameClub_doc);
            content_Doc = itemView.findViewById(R.id.content_Doc);
            btnJoin = itemView.findViewById(R.id.btnJoin);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Document document = documentList.get(position);
        holder.nameClub_doc.setText(document.getTitle());
        holder.content_Doc.setText(document.getContent());
        url_Join =  document.getFileUrl();
        holder.btnJoin.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url_Join));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }
}
