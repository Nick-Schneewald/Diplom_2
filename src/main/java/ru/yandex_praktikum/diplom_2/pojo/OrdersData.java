package ru.yandex_praktikum.diplom_2.pojo;

import java.util.ArrayList;
import java.util.Date;

public class OrdersData {
    private String _id;
    private ArrayList<String> ingredients;
    private String status;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private int number;

    public OrdersData() {
    }

    public OrdersData(String _id, ArrayList<String> ingredients, String status, String name, Date createdAt, Date updatedAt, int number) {
        this._id = _id;
        this.ingredients = ingredients;
        this.status = status;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.number = number;
    }

    public String get_id() {
        return _id;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int getNumber() {
        return number;
    }
}
