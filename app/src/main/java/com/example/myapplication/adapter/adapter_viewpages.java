package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragment.fragment_group_sv;
import com.example.myapplication.fragment.fragment_home_page;
import com.example.myapplication.fragment.fragment_page_tai_lieu;
import com.example.myapplication.fragment.fragment_trang_thong_bao;

public class adapter_viewpages extends FragmentStateAdapter {
    public adapter_viewpages(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new fragment_home_page();
            case 1:
                return new fragment_group_sv();
            case 2:
                return new fragment_page_tai_lieu();
            case 3:
                return new fragment_trang_thong_bao();
            default:
                return new fragment_home_page();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}


