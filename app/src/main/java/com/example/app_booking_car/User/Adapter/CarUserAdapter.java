package com.example.app_booking_car.User.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_booking_car.Model.Car;
import com.example.app_booking_car.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarUserAdapter extends RecyclerView.Adapter<CarUserAdapter.CarViewHolder> {

    private List<Car> cars;
    private OnBookButtonClickListener bookButtonClickListener;

    public interface OnBookButtonClickListener {
        void onBookButtonClick(int seatNumber, String carId);
    }

    public void setOnBookButtonClickListener(OnBookButtonClickListener listener) {
        this.bookButtonClickListener = listener;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_user, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);

        holder.carLicensePlate.setText("Biển số xe: " + car.getLicensePlate());
        holder.carDriver.setText("Lái xe: " + car.getDriver());
        holder.carRoute.setText("Tuyến xe: " + car.getRoute());
        holder.carPrice.setText("Giá: " + car.getPrice() + " VND");
        holder.carDepartureTime.setText("Giờ khởi hành: " + car.getDepartureTime());
        holder.carEstimatedTime.setText("Giờ dự kiến: " + car.getEstimatedTime());
        holder.carDepartureDate.setText("Ngày khởi hành: " + car.getDepartureDate());
        holder.carSeatNumber.setText("Số ghế: " + car.getSeatNumber());

        Picasso.get().load(car.getImage()).into(holder.carImage);

        holder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookButtonClickListener != null) {
                    int seatNumber = car.getSeatNumber();
                    String carId = car.getId();
                    bookButtonClickListener.onBookButtonClick(seatNumber, carId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars == null ? 0 : cars.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView carLicensePlate;
        TextView carDriver;
        TextView carRoute;
        TextView carPrice;
        TextView carDepartureTime;
        TextView carEstimatedTime;
        TextView carDepartureDate;
        TextView carSeatNumber;
        ImageView carImage;
        Button btnBook;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carLicensePlate = itemView.findViewById(R.id.carLicensePlate);
            carDriver = itemView.findViewById(R.id.carDriver);
            carRoute = itemView.findViewById(R.id.carRoute);
            carPrice = itemView.findViewById(R.id.carPrice);
            carDepartureTime = itemView.findViewById(R.id.carDepartureTime);
            carEstimatedTime = itemView.findViewById(R.id.carEstimatedTime);
            carDepartureDate = itemView.findViewById(R.id.carDepartureDate);
            carSeatNumber = itemView.findViewById(R.id.carSeatNumber);
            carImage = itemView.findViewById(R.id.carImage);
            btnBook = itemView.findViewById(R.id.btnHome);
        }
    }
}
