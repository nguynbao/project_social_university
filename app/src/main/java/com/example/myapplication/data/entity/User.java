package com.example.myapplication.data.entity;

import static androidx.room.ForeignKey.*;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table", foreignKeys = @ForeignKey(entity = Role.class,
                                                                parentColumns = "id",
                                                                childColumns = "role_id",
                                                                onDelete = ForeignKey.CASCADE))
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;  // Họ tên sinh viên
    private String email;
    private String password;
    private String mssv;
    private String maLop;
    private String phone;
    @ColumnInfo(name = "role_id")
    private int roleId; // có thể dùng String hoặc int tùy bạn

    public User() {

    }
    // Constructor có tham số - tên tham số phải đúng với tên field

    public User(String username, String email, String password, String mssv, String maLop, String phone, int roleId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mssv = mssv;
        this.maLop = maLop;
        this.phone = phone;
        this.roleId = roleId;
    }

    // Constructor mặc định bắt buộc cho Room
    @Ignore
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter và Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
