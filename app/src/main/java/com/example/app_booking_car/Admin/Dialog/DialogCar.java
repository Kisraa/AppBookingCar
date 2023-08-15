package com.example.app_booking_car.Admin.Dialog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.app_booking_car.Model.Car;
import com.example.app_booking_car.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class DialogCar extends DialogFragment {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText editTextLicensePlate;
    private EditText editTextDriver;
    private EditText editTextRoute;
    private EditText editTextPrice;
    private EditText editTextDepartureTime;
    private EditText editTextEstimatedTime;
    private EditText editTextImage;
    private Spinner spinnerSeatNumber;
    private ImageButton imageView;
    private Button btnAddCar;
    private EditText editTextDepartureDate;

    private FirebaseFirestore db;
    private CollectionReference carsCollection;

    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    private OnCarAddedListener onCarAddedListener;
    private Car carToEdit;

    public interface OnCarAddedListener {
        void onCarAdded(ArrayList<Car> newCarList);
    }

    public void setOnCarAddedListener(OnCarAddedListener listener) {
        this.onCarAddedListener = listener;
    }

    public void setCarToEdit(Car car) {
        this.carToEdit = car;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_car, container, false);

        db = FirebaseFirestore.getInstance();
        carsCollection = db.collection("cars");

        editTextLicensePlate = rootView.findViewById(R.id.editTextLicensePlate);
        editTextDriver = rootView.findViewById(R.id.editTextDriver);
        editTextRoute = rootView.findViewById(R.id.editTextRoute);
        editTextPrice = rootView.findViewById(R.id.editTextPrice);
        editTextDepartureTime = rootView.findViewById(R.id.editTextDepartureTime);
        editTextEstimatedTime = rootView.findViewById(R.id.editTextEstimatedTime);
        editTextImage = rootView.findViewById(R.id.editTextImage);
        spinnerSeatNumber = rootView.findViewById(R.id.spinnerSeatNumber);
        imageView = rootView.findViewById(R.id.imageView);
        btnAddCar = rootView.findViewById(R.id.btnAddCar);
        editTextDepartureDate = rootView.findViewById(R.id.editTextDepartureDate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.seat_numbers,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeatNumber.setAdapter(adapter);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        if (carToEdit != null) {
            // Nếu chỉnh sửa xe đã tồn tại, điền thông tin vào các trường
            editTextLicensePlate.setText(carToEdit.getLicensePlate());
            editTextDriver.setText(carToEdit.getDriver());
            editTextRoute.setText(carToEdit.getRoute());
            editTextPrice.setText(String.valueOf(carToEdit.getPrice()));
            editTextDepartureTime.setText(carToEdit.getDepartureTime());
            editTextEstimatedTime.setText(carToEdit.getEstimatedTime());
            editTextImage.setText(carToEdit.getImage());
            spinnerSeatNumber.setSelection(adapter.getPosition(String.valueOf(carToEdit.getSeatNumber())));
            editTextDepartureDate.setText(carToEdit.getDepartureDate());
            btnAddCar.setText("Lưu Thay Đổi");
        }

        editTextDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String licensePlate = editTextLicensePlate.getText().toString();
                String driver = editTextDriver.getText().toString();
                String route = editTextRoute.getText().toString();
                int price = Integer.parseInt(editTextPrice.getText().toString());
                String departureTime = editTextDepartureTime.getText().toString();
                String estimatedTime = editTextEstimatedTime.getText().toString();
                String image = editTextImage.getText().toString();
                int seatNumber = Integer.parseInt(spinnerSeatNumber.getSelectedItem().toString());

                String id = (licensePlate + driver + route + price + departureTime + estimatedTime + seatNumber).replace(" ", "");
                Car car = new Car(id,
                        licensePlate,
                        driver,
                        route,
                        price,
                        image,
                        departureTime,
                        estimatedTime,
                        seatNumber,
                        editTextDepartureDate.getText().toString()
                );

                if (carToEdit != null) {
                    // Nếu đang chỉnh sửa xe đã tồn tại, thực hiện cập nhật thông tin xe
                    updateCarInFirestore(car);
                } else {
                    // Nếu thêm xe mới, thực hiện thêm vào Firestore
                    saveCarToFirestore(car);
                }

                resetInput();

            }
        });

        return rootView;
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        editTextDepartureDate.setText(dateFormatter.format(calendar.getTime()));
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void saveCarToFirestore(Car car) {
        carsCollection.add(car)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Thêm xe thành công!", Toast.LENGTH_SHORT).show();

                            if (onCarAddedListener != null) {
                                onCarAddedListener.onCarAdded(new ArrayList<>(Collections.singletonList(car)));
                            }
                        } else {
                            Toast.makeText(getActivity(), "Thêm xe thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateCarInFirestore(Car car) {
        carsCollection.document(carToEdit.getId()).set(car)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Cập nhật thông tin xe thành công!", Toast.LENGTH_SHORT).show();

                            if (onCarAddedListener != null) {
                                onCarAddedListener.onCarAdded(new ArrayList<>(Collections.singletonList(car)));
                            }
                        } else {
                            Toast.makeText(getActivity(), "Cập nhật thông tin xe thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resetInput() {
        editTextLicensePlate.setText("");
        editTextDriver.setText("");
        editTextRoute.setText("");
        editTextPrice.setText("");
        editTextDepartureTime.setText("");
        editTextDepartureDate.setText("");
        editTextEstimatedTime.setText("");
        editTextImage.setText("");
        spinnerSeatNumber.setSelection(0);
        btnAddCar.setText("Thêm Xe");
        carToEdit = null;
    }
}
