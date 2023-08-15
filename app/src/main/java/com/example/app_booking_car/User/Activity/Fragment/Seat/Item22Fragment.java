package com.example.app_booking_car.User.Activity.Fragment.Seat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_booking_car.Model.Booking;
import com.example.app_booking_car.Model.Car;
import com.example.app_booking_car.Model.SeatLayout;
import com.example.app_booking_car.R;
import com.example.app_booking_car.User.ProjectUser.ProjectUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Item22Fragment extends Fragment implements View.OnClickListener {

    private Button[] buttons;
    private Map<String, Boolean> selectedSeatsMap = new HashMap<>();
    private String carId;

    public Item22Fragment() {
    }

    public Item22Fragment(String carId) {
        this.carId = carId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item22, container, false);

        // Ánh xạ các button ghế A1 đến B12
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
                view.findViewById(R.id.buttonB12)
        };
        //Kiểm tra ghế
        check(carId);

        // Thiết lập sự kiện onClick cho các button
        for (Button button : buttons) {
            button.setOnClickListener(this);
        }

        Button buttonBuy = view.findViewById(R.id.buttonBuy);
        buttonBuy.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonBuy) {
            // Người dùng nhấn vào nút Mua Vé
            showDetail();
        } else {
            // Xử lý sự kiện khi người dùng chọn ghế
            int selectedButtonIndex = -1;
            for (int i = 0; i < buttons.length; i++) {
                if (v.getId() == buttons[i].getId()) {
                    selectedButtonIndex = i;
                    break;
                }
            }

            if (selectedButtonIndex != -1) {
                String seatNumber = "Ghế " + buttons[selectedButtonIndex].getText().toString();
                if (selectedSeatsMap.containsKey(seatNumber)) {
                    // Nếu ghế đã được chọn trước đó, hủy chọn ghế đó
                    buttons[selectedButtonIndex].setBackgroundColor(Color.parseColor("#FF6750A3"));
                    selectedSeatsMap.remove(seatNumber);
                } else {
                    // Đổi màu của ghế đã chọn để người dùng nhận biết
                    buttons[selectedButtonIndex].setBackgroundColor(Color.parseColor("#00FF00"));
                    selectedSeatsMap.put(seatNumber, true);
                }
            }
        }
    }

    private void showDetail() {
        // Lấy thông tin người dùng hiện tại từ Firestore
        ProjectUser.getCurrentUserInformation(new ProjectUser.OnUserInformationReceivedListener() {
            @Override
            public void onUserInformationReceived(String name, String email, String phoneNumber, String address) {
                retrieveCarDetails(name, email, phoneNumber, address);
            }

            @Override
            public void onUserInformationError(String errorMessage) {
                showErrorMessage(errorMessage);
            }
        });
    }

    private void retrieveCarDetails(String name, String email, String phoneNumber, String address) {
        // Khởi tạo Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String a =carId;

        db.collection("cars")
                .whereEqualTo("id", carId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Lặp qua tất cả các tài liệu phù hợp
                            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                // Lấy giá trị "id" từ DocumentSnapshot
                                String idInDocument = documentSnapshot.getString("id");
                                if (idInDocument != null && idInDocument.equals(carId)) {
                                    Car car = documentSnapshot.toObject(Car.class);
                                    showCarDetailsNotification(name, email, phoneNumber, address, car);
                                    return; // Thoát khỏi vòng lặp nếu đã tìm thấy tài liệu phù hợp
                                }
                            }
                            // Nếu không tìm thấy tài liệu phù hợp
                            showErrorMessage("Không tìm thấy thông tin xe hợp lệ.");
                        } else {
                            // Nếu không có tài liệu phù hợp
                            showErrorMessage("Không tìm thấy thông tin xe hợp lệ.");
                        }
                    } else {
                        // Gọi phương thức onCarIdError() nếu có lỗi trong quá trình truy vấn
                        showErrorMessage("Lỗi khi truy vấn thông tin xe.");
                    }
                });

    }

    private void showCarDetailsNotification(String name, String email, String phoneNumber, String address, Car car) {
        // Lấy danh sách ghế đã chọn từ selectedSeatsMap
        ArrayList<String> selectedSeats = new ArrayList<>(selectedSeatsMap.keySet());

        // Tạo chuỗi thông tin ghế đã chọn
        StringBuilder selectedSeatsText = new StringBuilder();
        for (String seat : selectedSeats) {
            selectedSeatsText.append(seat).append(", ");
        }

        // Tính tổng số tiền
        int totalPrice = car.getPrice() * selectedSeats.size();

        // Tạo nội dung thông báo
        String message = "Ghế đã chọn: " + selectedSeatsText.toString() + "\n\n"
                + "Tổng số ghế: " + selectedSeats.size() + "\n"
                + "Tổng số tiền: " + totalPrice + " VNĐ\n\n"
                + "Thông tin người dùng:\nTên: " + name + "\nEmail: " + email
                + "\nSố điện thoại: " + phoneNumber + "\nĐịa chỉ: " + address
                + "\n\nThông tin xe:\nBiển số: " + car.getLicensePlate()
                + "\nTài xế: " + car.getDriver() + "\nTuyến đường: " + car.getRoute()
                + "\nGiá vé: " + car.getPrice() + "\nGiờ xuất phát: " + car.getDepartureTime()
                + "\nThời gian dự kiến: " + car.getEstimatedTime()
                + "\nSố chỗ ngồi: " + car.getSeatNumber() + "\nNgày xuất phát: " + car.getDepartureDate();

        // Tạo và hiển thị thông báo
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Nội Dung Vé");
        builder.setMessage(message);
        String id = name+ email+ phoneNumber+ address+ car+ selectedSeats+ totalPrice;
        // Xử lý sự kiện khi người dùng nhấn nút "Mua vé"
        builder.setPositiveButton("Mua vé", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lưu thông tin đặt vé vào Firebase Firestore
                saveBookingToFirestore(id,name, email, phoneNumber, address, car, selectedSeats, totalPrice);
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void saveBookingToFirestore(String id,String name, String email, String phoneNumber, String address, Car car, ArrayList<String> selectedSeats, int totalAmount) {
        // Khởi tạo Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo đối tượng Booking và đặt các thuộc tính
        Booking newBooking = new Booking();
        newBooking.setId(id);
        newBooking.setCarId(carId);
        newBooking.setSeatNumber(selectedSeats.toString()); // Store the seat numbers as a String
        newBooking.setQuantity(selectedSeatsMap.size());
        newBooking.setTotalAmount(totalAmount);
        newBooking.setName(name);
        newBooking.setEmail(email);
        newBooking.setPhoneNumber(phoneNumber);
        newBooking.setAddress(address);

        // Thêm thông tin đặt vé vào Firestore
        db.collection("bookings")
                .add(newBooking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Lưu layout của các ghế đã chọn vào Firestore
                        saveSeatLayout(carId, createLayout(selectedSeats));

                        // Hiển thị thông báo mua vé thành công
                        showSuccessMessage();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Hiển thị thông báo lỗi khi không lưu được thông tin đặt vé
                        showErrorMessage("Lỗi khi lưu thông tin đặt vé.");
                    }
                });
    }

    private Map<String, Boolean> createLayout(ArrayList<String> selectedSeats) {
        Map<String, Boolean> layout = new HashMap<>();

        // Đánh dấu các ghế đã chọn trong layout
        for (String seat : selectedSeats) {
            layout.put(seat, true);
        }

        return layout;
    }

    private void saveSeatLayout(String id, Map<String, Boolean> layout) {
        // Khởi tạo Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Kiểm tra xem id của seatlayout đã có trong Firestore hay chưa
        db.collection("seatLayouts")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // SeatLayout đã tồn tại, cập nhật layout
                        addNewLayoutData(id, layout);
                    } else {
                        // SeatLayout chưa tồn tại, tạo mới
                        createNewSeatLayout(id, layout);
                    }
                })
                .addOnFailureListener(e -> {
                    showErrorMessage("Lỗi khi kiểm tra tình trạng seatlayout.");
                });
    }

    private void addNewLayoutData(String id, Map<String, Boolean> newData) {
        // Khởi tạo Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tìm tới document có id tương ứng trong collection "seatLayouts"
        DocumentReference seatLayoutRef = db.collection("seatLayouts").document(id);

        // Lấy dữ liệu hiện tại của trường layout từ Firestore
        seatLayoutRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Lấy dữ liệu hiện tại từ document
                        Map<String, Boolean> currentLayout = (Map<String, Boolean>) documentSnapshot.get("layout");

                        // Thêm mới dữ liệu vào danh sách hiện tại
                        currentLayout.putAll(newData);

                        // Cập nhật trường layout với danh sách mới
                        seatLayoutRef
                                .update("layout", currentLayout)
                                .addOnSuccessListener(aVoid -> {
                                    // Xử lý thành công khi thêm mới dữ liệu vào trường layout
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý lỗi khi không thể thêm mới dữ liệu vào trường layout
                                    showErrorMessage("Lỗi khi thêm mới dữ liệu vào trường layout của seatlayout.");
                                });
                    } else {
                        // Xử lý lỗi nếu không tìm thấy document
                        showErrorMessage("Không tìm thấy document với id tương ứng.");
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu không thể lấy dữ liệu từ Firestore
                    showErrorMessage("Lỗi khi lấy dữ liệu từ Firestore.");
                });
    }





    private void createNewSeatLayout(String id, Map<String, Boolean> layout) {
        // Khởi tạo Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo seatlayout mới với layout
        SeatLayout newSeatLayout = new SeatLayout();
        newSeatLayout.setLayout(layout);
        newSeatLayout.setId(id);

        // Lưu seatlayout mới vào Firestore
        db.collection("seatLayouts")
                .document(id)
                .set(newSeatLayout)
                .addOnSuccessListener(aVoid -> {
                    // Xử lý thành công khi tạo mới seatlayout
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi không thể tạo mới seatlayout
                    showErrorMessage("Lỗi khi tạo mới seatlayout.");
                });
    }

    private void showErrorMessage(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Lỗi");
        builder.setMessage(errorMessage);
        builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Mua vé thành công");
        builder.setMessage("Bạn đã mua vé thành công!");

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            // Tải lại Activity
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        });

        builder.show();
    }

    private void check(String id) {
        // Khởi tạo Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Kiểm tra xem id của seatlayout đã có trong Firestore hay chưa
        db.collection("seatLayouts")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // SeatLayout đã tồn tại
                        SeatLayout seatLayout = documentSnapshot.toObject(SeatLayout.class);
                        Map<String, Boolean> layout = seatLayout.getLayout();

                        // Xử lý trạng thái của các ghế và vô hiệu hóa các button tương ứng
                        for (int i = 0; i < buttons.length; i++) {
                            String seatNumber = "Ghế " + buttons[i].getText().toString();
                            boolean isSeatAvailable = layout.containsKey(seatNumber) && layout.get(seatNumber);

                            if (isSeatAvailable==true) {
                                buttons[i].setEnabled(false); // Vô hiệu hóa button
                                buttons[i].setBackgroundColor(Color.RED); // Chuyển màu nền thành đỏ
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    showErrorMessage("Lỗi khi kiểm tra tình trạng seatlayout.");
                });
    }
}
