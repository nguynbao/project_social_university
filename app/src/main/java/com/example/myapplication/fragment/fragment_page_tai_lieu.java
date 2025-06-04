package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.myapplication.adapter.adapter_document;
import com.example.myapplication.component.menu;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.dao.DocumentDao;
import com.example.myapplication.data.entity.Document;
import com.example.myapplication.data.entity.Post;

import java.util.List;
import java.util.concurrent.Executors;

public class fragment_page_tai_lieu extends Fragment {

    public fragment_page_tai_lieu() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_tai_lieu, container, false);
        RecyclerView recycler_documents = view.findViewById(R.id.recycler_documents);
        recycler_documents.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView menuhp_doc = view.findViewById(R.id.menuhp_doc);
        menuhp_doc.setOnClickListener(view1 -> {
            menu.openMenu(getContext(), view1);
        });
        Executors.newSingleThreadExecutor().execute(()->{
            AppDatabase db = AppDatabase.getDatabase(getContext());
            DocumentDao documentDao = db.documentDao();
            List<Document> documentList = documentDao.getAllDocuments();
            for (Document document : documentList){
                Log.d("Docname", document.getTitle());
            }
            adapter_document adapterDocument = new adapter_document(documentList, getContext());
            recycler_documents.setAdapter(adapterDocument);
        });
        return view;
    }
}
