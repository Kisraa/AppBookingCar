package com.example.app_booking_car.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_booking_car.Admin.Activity.AdminHomeActivity;
import com.example.app_booking_car.User.Activity.HomeActivity;
import com.example.app_booking_car.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private TextView textViewForgotPassword, textViewRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewRegister = findViewById(R.id.textViewRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        if (user.isEmailVerified()) {
                                            // Đăng nhập thành công và email đã được xác thực, chuyển đến trang home.
                                            checkUserRoleAndNavigate(user.getUid());
                                        } else {
                                            // Hiển thị thông báo yêu cầu xác thực email.
                                            Toast.makeText(LoginActivity.this, "Vui lòng xác minh địa chỉ email của bạn.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    // Xử lý trường hợp đăng nhập không thành công.
                                    Toast.makeText(LoginActivity.this, "Xác thực không thành công.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Xử lý khi người dùng nhấp vào "Quên mật khẩu"
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý logic ở đây, ví dụ chuyển đến màn hình Quên mật khẩu (ForgotPasswordActivity)
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        // Xử lý khi người dùng nhấp vào "Chưa có tài khoản? Đăng ký ngay"
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý logic ở đây, ví dụ chuyển đến màn hình Đăng ký (RegisterActivity)
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void checkUserRoleAndNavigate(String userId) {
        db.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String role = document.getString("role");
                                if (role != null) {
                                    if (role.equals("admin")) {
                                        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    }
                                    finish();
                                } else {
                                    // Xử lý trường hợp không tìm thấy trường "role".
                                    Toast.makeText(LoginActivity.this, "Không tìm thấy quyền hạn người dùng.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Xử lý trường hợp không tìm thấy thông tin người dùng trong Firestore.
                                Toast.makeText(LoginActivity.this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Xử lý trường hợp lỗi khi truy vấn Firestore.
                            Toast.makeText(LoginActivity.this, "Lỗi khi truy vấn dữ liệu.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
