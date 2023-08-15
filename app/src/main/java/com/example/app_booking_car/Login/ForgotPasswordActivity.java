package com.example.app_booking_car.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_booking_car.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        TextView textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng nhấn vào TextView "Quay lại Đăng nhập"
                BackLogin.navigateToLoginActivity(ForgotPasswordActivity.this);
            }
        });

        editTextEmail = findViewById(R.id.editTextEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Gửi email khôi phục mật khẩu thành công.
                                    Toast.makeText(ForgotPasswordActivity.this, "Email đặt lại mật khẩu đã gửi. Vui lòng kiểm tra email của bạn.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Xử lý trường hợp gửi email khôi phục mật khẩu không thành công.
                                    Toast.makeText(ForgotPasswordActivity.this, "Không gửi được email đặt lại mật khẩu.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
