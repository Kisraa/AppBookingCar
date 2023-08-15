package com.example.app_booking_car.Detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.app_booking_car.Admin.Seat.Item22AdminFragment;
import com.example.app_booking_car.Admin.Seat.Item34AdminFragment;
import com.example.app_booking_car.R;
import com.example.app_booking_car.User.Activity.Fragment.Ticket.TicketFragment;
import com.example.app_booking_car.User.Activity.Fragment.Seat.Item22Fragment;
import com.example.app_booking_car.User.Activity.Fragment.Seat.Item34Fragment;

public class SeatDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_detail);

        // Tìm nút back trong layout
        ImageButton btnBack = findViewById(R.id.btnBack);

        // Thiết lập click listener cho nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Inside
        int seatNumber = getIntent().getIntExtra("seatNumber", 0);
        String carId = getIntent().getStringExtra("carId");
        String form = getIntent().getStringExtra("form");

        if ("userForm".equals(form)) {
            if (seatNumber == 22) {
                Item22Fragment fragment = new Item22Fragment(carId);
                loadFragment(fragment);
            } else if (seatNumber == 34) {
                Item34Fragment fragment = new Item34Fragment(carId);
                loadFragment(fragment);
            }
        } else if("userAdmin".equals(form)){
            if (seatNumber == 22) {
                Item22AdminFragment fragment = new Item22AdminFragment(carId);
                loadFragment(fragment);
            } else if (seatNumber == 34) {
                Item34AdminFragment fragment = new Item34AdminFragment(carId);
                loadFragment(fragment);
            }
        } else
        {
            TicketFragment fragment = new TicketFragment();
            loadFragment(fragment);
        }





    }

    // Phương thức để tải một fragment
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

}
