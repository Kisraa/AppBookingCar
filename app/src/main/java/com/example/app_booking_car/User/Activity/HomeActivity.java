package com.example.app_booking_car.User.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.app_booking_car.Admin.Activity.Fragment.SettingsFragment;
import com.example.app_booking_car.Login.LoginActivity;
import com.example.app_booking_car.Model.Car;
import com.example.app_booking_car.R;
import com.example.app_booking_car.User.Adapter.CarUserAdapter;
import com.example.app_booking_car.Detail.SeatDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private CarUserAdapter carUserAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Khởi tạo RecyclerView
        RecyclerView recyclerView = findViewById(R.id.myrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo CarUserAdapter
        carUserAdapter = new CarUserAdapter();
        recyclerView.setAdapter(carUserAdapter);

        // Tải danh sách xe từ Firestore
        loadCars();

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        // Sự kiện khi người dùng chọn đặt vé
        carUserAdapter.setOnBookButtonClickListener(new CarUserAdapter.OnBookButtonClickListener() {
            @Override
            public void onBookButtonClick(int seatNumber, String carId) {
                // Chuyển sang SeatDetailActivity và truyền thông tin về ghế ngồi (seatNumber) và id xe (carId) cùng với thông tin form
                Intent intent = new Intent(HomeActivity.this, SeatDetailActivity.class);
                intent.putExtra("seatNumber", seatNumber);
                intent.putExtra("carId", carId);
                intent.putExtra("form", "userForm");
                startActivity(intent);
            }
        });


    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Thay 'this' bằng context phù hợp
        builder.setTitle("Cài đặt cá nhân");
        mAuth = FirebaseAuth.getInstance();
        builder.setPositiveButton("Đăng Xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện logout
                mAuth.signOut();

                // Chuyển hướng đến màn hình đăng nhập
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);

                // Đóng activity hiện tại (SettingActivity)
                finish();
            }
        });

        builder.setNeutralButton("Thông tin người dùng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNegativeButton("Vé đã mua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(HomeActivity.this, SeatDetailActivity.class);
                intent.putExtra("seatNumber", 2);
                intent.putExtra("carId", "a");
                intent.putExtra("form", "ticket");
                startActivity(intent);
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void loadCars() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cars")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Car> carList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Car car = document.toObject(Car.class);
                                carList.add(car);
                            }
                            // Cập nhật RecyclerView với danh sách xe mới
                            carUserAdapter.setCars(carList);
                            carUserAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("HomeActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void loadSettingsFragment() {
        // Tạo một instance của SettingsFragment
        SettingsFragment settingsFragment = new SettingsFragment();
        // Sử dụng FragmentManager để thêm SettingsFragment vào trong fragmentContainer
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, settingsFragment)
                .addToBackStack(null)
                .commit();
    }
}
