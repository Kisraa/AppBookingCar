package com.example.app_booking_car.Admin.Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_booking_car.Admin.Adapter.CarAdapter;
import com.example.app_booking_car.Admin.Dialog.DialogCar;
import com.example.app_booking_car.Model.Car;
import com.example.app_booking_car.R;
import com.example.app_booking_car.Detail.SeatDetailActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddCarFragment extends Fragment implements DialogCar.OnCarAddedListener {

    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private ArrayList<Car> carList;

    private FirebaseFirestore db;
    private CollectionReference carsCollection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_car, container, false);

        db = FirebaseFirestore.getInstance();
        carsCollection = db.collection("cars");

        carList = new ArrayList<>();
        carAdapter = new CarAdapter(carList);

        recyclerView = rootView.findViewById(R.id.recyclerViewCars);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(carAdapter);

        carAdapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                openDialog(carList.get(position));
            }

            @Override
            public void onDeleteClick(int position) {
                deleteCar(position);
            }
        });

        carAdapter.setOnBookButtonClickListener(new CarAdapter.OnBookButtonClickListener() {
            @Override
            public void onBookButtonClick(int seatNumber, String carId) {
                Intent intent = new Intent(requireContext(), SeatDetailActivity.class);
                intent.putExtra("seatNumber", seatNumber);
                intent.putExtra("carId", carId);
                intent.putExtra("form", "userAdmin");
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(null);
            }
        });

        loadCarsFromFirestore();

        return rootView;
    }

    private void openDialog(Car car) {
        DialogCar dialogFragment = new DialogCar();
        dialogFragment.setOnCarAddedListener(this);
        if (car != null) {
            dialogFragment.setCarToEdit(car);
        }
        dialogFragment.show(getParentFragmentManager(), "CarDialogFragment");
    }

    private void deleteCar(int position) {
        Car carToDelete = carList.get(position);
        carsCollection.document(carToDelete.getId()).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), "Xóa xe thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Xóa xe thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCarsFromFirestore() {
        carsCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                // Xử lý lỗi ở đây (nếu có)
                return;
            }

            if (queryDocumentSnapshots != null) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                carList.clear();
                for (DocumentSnapshot snapshot : documentSnapshots) {
                    Car car = snapshot.toObject(Car.class);
                    car.setId(snapshot.getId());
                    carList.add(car);
                }
                carAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCarAdded(ArrayList<Car> newCarList) {
        carList.clear();
        carList.addAll(newCarList);
        carAdapter.notifyDataSetChanged();
    }
}
