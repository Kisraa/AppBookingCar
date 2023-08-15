package com.example.app_booking_car.Model;

public class Booking {
    private String id;
    private String carId; // ID của xe được đặt vé
    private String seatNumber; // Vị trí ghế được chọn
    private int quantity; // Số lượng ghế đã đặt
    private int totalAmount; // Tổng giá cho đơn đặt vé
    private String name; // Tên của khách hàng
    private String email; // Địa chỉ email của khách hàng
    private String phoneNumber; // Số điện thoại của khách hàng
    private String address; // Địa chỉ của khách hàng

    public Booking() {
    }

    public Booking(String id, String carId, String seatNumber, int quantity, int totalAmount,
                   String name, String email, String phoneNumber, String address) {
        this.id = id;
        this.carId = carId;
        this.seatNumber = seatNumber;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getCarId() {
        return carId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

