package com.example.app_booking_car.Model;

import java.util.Map;

public class SeatLayout {
    private String id;
    private Map<String, Boolean> layout; // Trạng thái ghế (true:đã đặt)

    public SeatLayout() {
    }

    public SeatLayout(String id, Map<String, Boolean> layout) {
        this.id = id;
        this.layout = layout;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Map<String, Boolean> getLayout() {
        return layout;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setLayout(Map<String, Boolean> layout) {
        this.layout = layout;
    }

    // Phương thức để lấy trạng thái của ghế cụ thể dựa vào vị trí ghế
    public boolean isSeatAvailable(String seatPosition) {
        if (layout != null && layout.containsKey(seatPosition)) {
            return layout.get(seatPosition);
        } else {
            return false;
        }
    }
}

