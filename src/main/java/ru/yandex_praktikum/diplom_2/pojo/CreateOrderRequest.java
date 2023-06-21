package ru.yandex_praktikum.diplom_2.pojo;

import java.util.List;

public class CreateOrderRequest {
    private List<String> ingredients;

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
