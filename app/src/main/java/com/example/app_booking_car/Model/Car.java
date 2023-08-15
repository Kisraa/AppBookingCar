package com.example.app_booking_car.Model;

public class Car {
    private String id;
    private String licensePlate;
    private String driver;
    private String route;
    private int price;
    private String image;
    private String departureTime;
    private String estimatedTime;
    private int seatNumber;
    private String departureDate;

    public Car(){

    }

    public Car(String id, String licensePlate, String driver, String route, int price, String image, String departureTime, String estimatedTime, int seatNumber, String departureDate) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.driver = driver;
        this.route = route;
        this.price = price;
        this.image = image;
        this.departureTime = departureTime;
        this.estimatedTime = estimatedTime;
        this.seatNumber = seatNumber;
        this.departureDate = departureDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", driver='" + driver + '\'' +
                ", route='" + route + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", estimatedTime='" + estimatedTime + '\'' +
                ", seatNumber=" + seatNumber +
                ", departureDate='" + departureDate + '\'' +
                '}';
    }
}