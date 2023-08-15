package com.example.app_booking_car.Admin.Activity.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_booking_car.Admin.Adapter.UserAdapter;
import com.example.app_booking_car.Model.User;
import com.example.app_booking_car.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment implements UserAdapter.EditButtonClickListener {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = rootView.findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        userAdapter.setEditButtonClickListener(this);
        recyclerView.setAdapter(userAdapter);

        // Load danh sách người dùng từ Firestore
        loadUsersFromFirestore();

        return rootView;
    }

    private void loadUsersFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    String email = document.getString("email");
                    String phoneNumber = document.getString("phoneNumber");
                    String role = document.getString("role");
                    String address = document.getString("address");

                    User user = new User(name, email, phoneNumber, role, address);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onEditButtonClick(User user) {
        showEditDialog(user);
    }

    private void showEditDialog(User user) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_user, null);
        dialogBuilder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPhoneNumber = dialogView.findViewById(R.id.editTextPhoneNumber);
        EditText editAddress = dialogView.findViewById(R.id.editAddress);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerRole);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Điền dữ liệu hiện tại của người dùng vào các trường trong hộp thoại
        editTextName.setText(user.getName());
        editTextPhoneNumber.setText(user.getPhoneNumber());
        editAddress.setText(user.getAddress());
        spinnerRole.setSelection(getRoleIndex(user.getRole()));

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        btnSave.setOnClickListener(view -> {
            // Cập nhật dữ liệu người dùng với dữ liệu mới
            String name = editTextName.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();
            String address = editAddress.getText().toString();
            String role = spinnerRole.getSelectedItem().toString();

            user.setName(name);
            user.setPhoneNumber(phoneNumber);
            user.setAddress(address);
            user.setRole(role);

            // Tìm tài liệu người dùng dựa trên trường email (hoặc phoneNumber) và cập nhật dữ liệu
            updateUserDataByEmail(user);

            // Đóng hộp thoại
            dialog.dismiss();

            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        });
    }

    private int getRoleIndex(String role) {
        String[] rolesArray = getResources().getStringArray(R.array.roles);
        for (int i = 0; i < rolesArray.length; i++) {
            if (rolesArray[i].equals(role)) {
                return i;
            }
        }
        return 0;
    }

    private void updateUserDataByEmail(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("email", user.getEmail()) // Thay "email" bằng trường duy nhất để xác định tài liệu người dùng
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            db.collection("users").document(documentId).set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        // Lưu dữ liệu thành công
                                        // Hiển thị thông báo hoặc thực hiện các tác vụ sau khi lưu dữ liệu thành công
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xảy ra lỗi khi lưu dữ liệu
                                        // Hiển thị thông báo hoặc thực hiện các tác vụ sau khi xảy ra lỗi
                                    });
                        }
                    }
                });
    }
}
