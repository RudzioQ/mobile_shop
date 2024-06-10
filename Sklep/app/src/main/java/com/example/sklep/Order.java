package com.example.sklep;

public class Order {
    private String name, email, phone, computerName, mouseName, keyboardName, webcamName, dateTime;
    private int quantity, totalPrice;

    public Order(String name, String email, String phone, String computerName, String mouseName,
                 String keyboardName, String webcamName, int quantity, int totalPrice, String dateTime) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.computerName = computerName;
        this.mouseName = mouseName;
        this.keyboardName = keyboardName;
        this.webcamName = webcamName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getMouseName() {
        return mouseName;
    }

    public void setMouseName(String mouseName) {
        this.mouseName = mouseName;
    }

    public String getKeyboardName() {
        return keyboardName;
    }

    public void setKeyboardName(String keyboardName) {
        this.keyboardName = keyboardName;
    }

    public String getWebcamName() {
        return webcamName;
    }

    public void setWebcamName(String webcamName) {
        this.webcamName = webcamName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
