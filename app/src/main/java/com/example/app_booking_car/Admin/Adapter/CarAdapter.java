package com.example.app_booking_car.Admin.Adapter;

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

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private ArrayList<Car> carList;
    private OnItemClickListener onItemClickListener;
    private OnBookButtonClickListener onBookButtonClickListener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public interface OnBookButtonClickListener {
        void onBookButtonClick(int seatNumber, String carId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnBookButtonClickListener(OnBookButtonClickListener listener) {
        this.onBookButtonClickListener = listener;
    }

    public CarAdapter(ArrayList<Car> carList) {
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.bind(car);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClickListener.onEditClick(adapterPosition);
                    }
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClickListener.onDeleteClick(adapterPosition);
                    }
                }
            }
        });

        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBookButtonClickListener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        int seatNumber = carList.get(adapterPosition).getSeatNumber();
                        String carId = carList.get(adapterPosition).getId();
                        onBookButtonClickListener.onBookButtonClick(seatNumber, carId);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        public TextView carLicensePlate;
        public TextView carDriver;
        public TextView carRoute;
        public TextView carPrice;
        public TextView carDepartureTime;
        public TextView carEstimatedTime;
        public TextView carDepartureDate;
        public TextView carSeatNumber;
        public ImageView carImage;
        public ImageView editButton;
        public ImageView deleteButton;
        public Button bookButton;

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
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            bookButton = itemView.findViewById(R.id.btnClick);
        }

        public void bind(Car car) {
            carLicensePlate.setText("Biển số xe: " + car.getLicensePlate());
            carDriver.setText("Lái xe: " + car.getDriver());
            carRoute.setText("Tuyến xe: " + car.getRoute());
            carPrice.setText("Giá: " + car.getPrice() + " VND");
            carDepartureTime.setText("Giờ khởi hành: " + car.getDepartureTime());
            carEstimatedTime.setText("Giờ dự kiến: " + car.getEstimatedTime());
            carDepartureDate.setText("Ngày khởi hành: " + car.getDepartureDate());
            carSeatNumber.setText("Số ghế: " + car.getSeatNumber());
            Picasso.get().load(car.getImage()).into(carImage);
        }
    }
}
