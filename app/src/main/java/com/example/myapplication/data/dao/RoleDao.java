package com.example.myapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.data.entity.Role;

import java.util.List;

@Dao
public interface RoleDao {
    @Insert
    void insert (Role role);

    @Query("SELECT * FROM role_table")
    List<Role> getAllRoles();

    @Query("SELECT COUNT(*) FROM role_table")
    int count();  // <- Tự tạo hàm này
}
