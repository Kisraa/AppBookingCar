package com.example.app_booking_car.Login;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class BackLogin {

    // Phương thức chuyển tới giao diện đăng nhập
    public static void navigateToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    // Phương thức đăng xuất
    public static void logout(Context context) {
        FirebaseAuth.getInstance().signOut();

        // Chuyển hướng đến trang đăng nhập
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
