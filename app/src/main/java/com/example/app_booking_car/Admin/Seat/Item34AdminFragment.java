package com.example.app_booking_car.Admin.Seat;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_booking_car.Admin.Adapter.BookingAdapter;
import com.example.app_booking_car.Model.Booking;
import com.example.app_booking_car.Model.SeatLayout;
import com.example.app_booking_car.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Item34AdminFragment extends Fragment {
    private Button[] buttons;

    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private RecyclerView recyclerView;
    private String carId;

    private FirebaseFirestore db;

    public Item34AdminFragment() {
    }

    public Item34AdminFragment(String carId) {
        this.carId = carId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item34_admin, container, false);

        // Ánh xạ các button ghế A1 đến B17
        buttons = new Button[]{
                view.findViewById(R.id.buttonA1),
                view.findViewById(R.id.buttonA2),
                view.findViewById(R.id.buttonA3),
                view.findViewById(R.id.buttonA4),
                view.findViewById(R.id.buttonA5),
                view.findViewById(R.id.buttonA6),
                view.findViewById(R.id.buttonA7),
                view.findViewById(R.id.buttonA8),
                view.findViewById(R.id.buttonA9),
                view.findViewById(R.id.buttonA10),
                view.findViewById(R.id.buttonA11),
                view.findViewById(R.id.buttonA12),
                view.findViewById(R.id.buttonA13),
                view.findViewById(R.id.buttonA14),
                view.findViewById(R.id.buttonA15),
                view.findViewById(R.id.buttonA16),
                view.findViewById(R.id.buttonA17),
                view.findViewById(R.id.buttonB1),
                view.findViewById(R.id.buttonB2),
                view.findViewById(R.id.buttonB3),
                view.findViewById(R.id.buttonB4),
                view.findViewById(R.id.buttonB5),
                view.findViewById(R.id.buttonB6),
                view.findViewById(R.id.buttonB7),
                view.findViewById(R.id.buttonB8),
                view.findViewById(R.id.buttonB9),
                view.findViewById(R.id.buttonB10),
                view.findViewById(R.id.buttonB11),
                view.findViewById(R.id.buttonB12),
                view.findViewById(R.id.buttonB13),
                view.findViewById(R.id.buttonB14),
                view.findViewById(R.id.buttonB15),
                view.findViewById(R.id.buttonB16),
                view.findViewById(R.id.buttonB17),
        };

        recyclerView = view.findViewById(R.id.recyclerViewB);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        bookingList = new ArrayList<>();

        // Lấy dữ liệu từ Firestore và cập nhật vào bookingList
        db.collection("bookings")
                .whereEqualTo("carId", carId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Xóa danh sách booking cũ trước khi thêm dữ liệu mới
                            bookingList.clear();

                            // Lặp qua tất cả các tài liệu phù hợp
                            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                Booking booking = documentSnapshot.toObject(Booking.class);
                                bookingList.add(booking);
                            }

                            // Cập nhật dữ liệu cho adapter sau khi lấy từ Firestore
                            bookingAdapter.notifyDataSetChanged();
                        } else {
                            // Nếu không có tài liệu phù hợp
                            Log.d("Firestore", "Không tìm thấy document..");
                        }
                    } else {
                        // Xử lý lỗi trong quá trình truy vấn
                        Log.d("Firestore", "Lỗi khi truy vấn bookings: " + task.getException());
                    }
                });

        bookingAdapter = new BookingAdapter(bookingList);
        recyclerView.setAdapter(bookingAdapter);
        check(carId);

        return view;
    }

    private void check(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("seatLayouts")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        SeatLayout seatLayout = documentSnapshot.toObject(SeatLayout.class);
                        Map<String, Boolean> layout = seatLayout.getLayout();

                        for (int i = 0; i < buttons.length; i++) {
                            String seatNumber = "Ghế " + buttons[i].getText().toString();
                            boolean isSeatAvailable = layout.containsKey(seatNumber) && layout.get(seatNumber);

                            if (isSeatAvailable) {
                                buttons[i].setEnabled(false);
                                buttons[i].setBackgroundColor(Color.RED);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    showErrorMessage("Lỗi khi kiểm tra tình trạng seatlayout.");
                });
    }

    private void showErrorMessage(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Lỗi");
        builder.setMessage(errorMessage);
        builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
