package com.example.app_booking_car.Admin.Activity.Fragment;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.app_booking_car.User.Activity.HomeActivity;
import com.example.app_booking_car.Login.LoginActivity;
import com.example.app_booking_car.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private Button btnLogout;
    private Button btnHome;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        btnLogout = rootView.findViewById(R.id.btnLogout);
        btnHome = rootView.findViewById(R.id.btnHome);
        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện logout
                mAuth.signOut();

                // Chuyển hướng đến màn hình đăng nhập
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                // Đóng fragment hiện tại (SettingsFragment)
                getActivity().finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển hướng đến màn hình HomeActivity (activity_home.xml)
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
