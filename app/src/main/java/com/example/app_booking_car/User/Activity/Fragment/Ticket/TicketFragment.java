package com.example.app_booking_car.User.Activity.Fragment.Ticket;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_booking_car.Admin.Adapter.BookingAdapter;
import com.example.app_booking_car.Model.Booking;
import com.example.app_booking_car.R;
import com.example.app_booking_car.User.ProjectUser.ProjectUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TicketFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ticket, container, false);

        recyclerView = rootView.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        bookingList = new ArrayList<>();

        ProjectUser.getCurrentUserInformation(new ProjectUser.OnUserInformationReceivedListener() {
            @Override
            public void onUserInformationReceived(String name, String email, String phoneNumber, String address) {
                // Lấy dữ liệu từ Firestore và cập nhật vào bookingList
                db.collection("bookings")
                        .whereEqualTo("email", email)
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
            }
            @Override
            public void onUserInformationError(String errorMessage) {
                Log.e("Không lấy được thông tin người dùng", errorMessage);
            }
        });
        return rootView;
    }
}