package com.example.myapplication.data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class UserWithRole {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "role_id",
            entityColumn = "id"
    )
    public Role role;
}
