package com.example.myapplication.data.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.entity.Document;

import java.util.List;

@Dao
public interface DocumentDao {
    @Insert
    void insert(Document document);

    @Update
    void update(Document document);

    @Delete
    void delete(Document document);

    @Query("SELECT * FROM document_table")
    List<Document> getAllDocuments();
    @Query("SELECT * FROM document_table")
    LiveData<List<Document>> getAllDocumentsbyliveData();
    @Query("SELECT * FROM document_table WHERE id = :documentId")
    Document getDocById(int documentId);
}
