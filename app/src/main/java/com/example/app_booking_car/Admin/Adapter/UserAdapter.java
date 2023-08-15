package com.example.app_booking_car.Admin.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_booking_car.Model.User;
import com.example.app_booking_car.R;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private EditButtonClickListener editButtonClickListener;

    public interface EditButtonClickListener {
        void onEditButtonClick(User user);
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    public void setEditButtonClickListener(EditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewFullName.setText(user.getName());
        holder.textViewEmail.setText(user.getEmail());
        holder.textViewPhoneNumber.setText(user.getPhoneNumber());
        holder.textViewAddress.setText(user.getAddress());
        holder.textViewRole.setText(user.getRole());

        // Xử lý sự kiện click nút chỉnh sửa
        holder.editButton.setOnClickListener(view -> {
            if (editButtonClickListener != null) {
                editButtonClickListener.onEditButtonClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFullName, textViewEmail, textViewPhoneNumber, textViewAddress, textViewRole;
        ImageButton editButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFullName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
