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
import com.example.app_booking_car.Model.User;
import com.example.app_booking_car.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword, editTextPhoneNumber, editTextAddress;
    private Button btnRegister;
    private TextView textViewLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextAddress = findViewById(R.id.editTextAddress);
        btnRegister = findViewById(R.id.btnRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getText().toString();
                final String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                final String phoneNumber = editTextPhoneNumber.getText().toString();
                final String address = editTextAddress.getText().toString();

                if (password.equals(confirmPassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            // Thêm thông tin người dùng vào Firestore hoặc Realtime Database nếu cần thiết.
                                            String role = "user";
                                            if (email.contains("admin.com")) {
                                                role = "admin";
                                            }
                                            final User newUser = new User(name, email, phoneNumber, role, address);

                                            db.collection("users")
                                                    .document(user.getUid())
                                                    .set(newUser)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // Ghi thông tin người dùng vào Firestore thành công.
                                                                // Tiếp tục gửi email xác thực và hiển thị thông báo đăng ký thành công.
                                                                sendEmailVerification();
                                                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công. Vui lòng kiểm tra email của bạn để xác minh.", Toast.LENGTH_SHORT).show();

                                                                // Chuyển hướng đến trang tương ứng với vai trò của người dùng.
                                                                if (newUser.getRole().equals("admin")) {
                                                                    startActivity(new Intent(RegisterActivity.this, AdminHomeActivity.class));
                                                                } else {
                                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                                }
                                                                finish(); // Đóng RegisterActivity sau khi chuyển hướng.
                                                            } else {
                                                                // Xử lý trường hợp thêm thông tin người dùng vào Firestore không thành công.
                                                                Toast.makeText(RegisterActivity.this, "Đăng ký không thành công.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Xử lý trường hợp đăng ký không thành công.
                                        Toast.makeText(RegisterActivity.this, "Đăng ký không thành công.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Xử lý trường hợp mật khẩu và xác nhận mật khẩu không khớp.
                    Toast.makeText(RegisterActivity.this, "Mật khẩu và Xác nhận mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý khi người dùng nhấp vào "Đăng nhập"
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý logic ở đây, ví dụ chuyển đến màn hình Đăng nhập (LoginActivity)
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Email xác thực đã được gửi thành công.
                                Toast.makeText(RegisterActivity.this, "Email xác thực đã được gửi. Vui lòng kiểm tra email của bạn.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Xử lý trường hợp gửi email xác thực không thành công.
                                Toast.makeText(RegisterActivity.this, "Không gửi được email xác thực.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
