package com.example.myapplication.data.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table",
            foreignKeys = @ForeignKey(entity = Role.class,
                                        parentColumns = "id",
                                        childColumns = "role_id",
                                        onDelete = ForeignKey.CASCADE))

public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;
    private String password;
    private String name;
    @ColumnInfo(name = "role_id")
    private int roleId;

    // Constructor
    public User(String username, String password, String name, int roleId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.roleId = roleId;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
}
