package com.example.app_booking_car.User.ProjectUser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProjectUser {

    // Interface dùng để xử lý thông tin người dùng được nhận từ Firestore
    public interface OnUserInformationReceivedListener {
        void onUserInformationReceived(String name, String email, String phoneNumber, String address);
        void onUserInformationError(String errorMessage);
    }

    // Phương thức để lấy thông tin người dùng hiện tại đang đăng nhập từ Firestore
    public static void getCurrentUserInformation(final OnUserInformationReceivedListener listener) {
        // Kiểm tra xem người dùng đã đăng nhập vào Firebase Authentication chưa
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Lấy ID người dùng đã đăng nhập từ Firebase Authentication
            String userId = currentUser.getUid();

            // Tạo kết nối đến Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Tạo tham chiếu đến tài liệu người dùng trong Firestore
            DocumentReference userRef = db.collection("users").document(userId);

            // Lấy thông tin người dùng từ Firestore
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Kiểm tra xem có tài liệu tồn tại không
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy thông tin người dùng từ DocumentSnapshot
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String phoneNumber = document.getString("phoneNumber");
                        String address = document.getString("address");

                        // Gọi callback để trả về thông tin người dùng
                        if (listener != null) {
                            listener.onUserInformationReceived(name, email, phoneNumber, address);
                        }
                    }
                } else {
                    // Xử lý lỗi khi không lấy được thông tin người dùng
                    if (listener != null) {
                        listener.onUserInformationError(task.getException().getMessage());
                    }
                }
            });
        }
    }
}

