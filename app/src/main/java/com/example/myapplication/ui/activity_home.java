package com.example.myapplication.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.adapter.adapter_viewpages;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class activity_home extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_trang_chu);
        viewpage = findViewById(R.id.viewpage);
        viewpage.setAdapter(new adapter_viewpages(this));
        viewpage.setUserInputEnabled(false);
        tabLayout = findViewById(R.id.tabLayout);
        ColorStateList iconColorStateList = ContextCompat.getColorStateList(this, R.color.selector_tab_icon);
        tabLayout.setTabIconTint(iconColorStateList);

        new TabLayoutMediator(tabLayout, viewpage, (tab,position) -> {
            switch (position){
                case 0:
                    tab.setText("Home");
                    tab.setIcon(R.drawable.icon_home);
                    break;
                case 1:
                    tab.setText("Group");
                    tab.setIcon(R.drawable.icon_group);
                    break;
                case 2:
                    tab.setText("Docs");
                    tab.setIcon(R.drawable.icon_documents);
                    break;
                case 3:
                    tab.setText("Noti");
                    tab.setIcon(R.drawable.icon_noti);
                    break;
            }
        }).attach();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}