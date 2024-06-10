package com.example.sklep;

public class Product {
    private final int imageResId;
    private final String name;
    private final int price;

    public Product(int imageResId, String name, int price) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name;
    }
}
