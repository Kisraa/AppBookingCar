package com.example.app_booking_car.Admin.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.app_booking_car.Admin.Activity.Fragment.AddCarFragment;
import com.example.app_booking_car.Admin.Activity.Fragment.ListFragment;
import com.example.app_booking_car.Admin.Activity.Fragment.SettingsFragment;
import com.example.app_booking_car.Admin.Activity.Fragment.UserFragment;
import com.example.app_booking_car.R;
import com.google.android.material.tabs.TabLayout;

public class AdminHomeActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_home);

            TabLayout tabLayout = findViewById(R.id.tabLayout);
            tabLayout.addTab(tabLayout.newTab().setText("Người dùng"));
            tabLayout.addTab(tabLayout.newTab().setText("Thêm Xe"));
            tabLayout.addTab(tabLayout.newTab().setText("Danh Sách"));
            tabLayout.addTab(tabLayout.newTab().setText("Cài đặt"));

            // Chọn trang đầu tiên khi mở ứng dụng
            replaceFragment(new UserFragment());

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            replaceFragment(new UserFragment());
                            break;
                        case 1:
                            replaceFragment(new AddCarFragment());
                            break;
                        case 2:
                            replaceFragment(new ListFragment());
                            break;
                        case 3:
                            replaceFragment(new SettingsFragment());
                            break;

                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }

        // Hàm thay thế Fragment trong container
        private void replaceFragment(Fragment fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
        }
}
