package com.example.myapplication.component;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.PopupMenu;

import com.example.myapplication.R;
import com.example.myapplication.ui.activity_login;

public class menu {
    public static void openMenu(Context context, View anchor) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                // Xử lý logout
                Intent intent = new Intent(context, activity_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xoá hết backstack
                context.startActivity(intent);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
}
