package com.example.app_booking_car.Admin.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_booking_car.Model.Booking;
import com.example.app_booking_car.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.textName.setText("Họ và tên: " + booking.getName());
        holder.textPhoneNumber.setText("SĐT: " + booking.getPhoneNumber());
        holder.textAddress.setText("Địa chỉ: " + booking.getAddress());
        holder.textSeatNumber.setText("Vị trí ghế: " + booking.getSeatNumber());
        holder.textTotalAmount.setText("Giá tiền: " + booking.getTotalAmount() + " VNĐ");

        String id = booking.getCarId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("cars")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                            // Lấy giá trị từ DocumentSnapshot
                            String route = documentSnapshot.getString("route");
                            String licensePlate = documentSnapshot.getString("licensePlate");
                            String departureDate = documentSnapshot.getString("departureDate");
                            String estimatedTime = documentSnapshot.getString("estimatedTime");

                            // Thiết lập giá trị vào holder
                            holder.textRoute.setText("Tuyến Xe: " + route);
                            holder.textLicensePlate.setText("Biển số xe: " + licensePlate);
                            holder.textDepartureDate.setText("Ngày khởi hành: " + departureDate);
                            holder.textEstimatedTime.setText("Giờ khởi hành: " + estimatedTime);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPhoneNumber, textAddress, textSeatNumber, textTotalAmount, textRoute, textLicensePlate, textDepartureDate, textEstimatedTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textPhoneNumber = itemView.findViewById(R.id.textPhoneNumber);
            textAddress = itemView.findViewById(R.id.textAddress);
            textSeatNumber = itemView.findViewById(R.id.textSeatNumber);
            textTotalAmount = itemView.findViewById(R.id.textTotalAmount);
            textRoute = itemView.findViewById(R.id.textRoute);
            textLicensePlate = itemView.findViewById(R.id.textLicensePlate);
            textDepartureDate = itemView.findViewById(R.id.textDepartureDate);
            textEstimatedTime = itemView.findViewById(R.id.textEstimatedTime);
        }
    }
}
