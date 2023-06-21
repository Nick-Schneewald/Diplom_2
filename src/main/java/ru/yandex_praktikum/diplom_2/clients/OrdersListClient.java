package ru.yandex_praktikum.diplom_2.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrdersListClient extends BaseClient {
    public static final String GET_ORDERS_LIST_HANDLE = "/api/orders";
    public static final String GET_ALL_ORDERS_HANDLE = "/api/orders/all";

    @Step("Получение заказов конкретного пользователя без авторизации")
    public ValidatableResponse getOrdersList() {
        return given()
                .spec(getSpec())
                .when()
                .get(GET_ORDERS_LIST_HANDLE)
                .then();
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public ValidatableResponse getOrdersList(String bearerToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(bearerToken)
                .when()
                .get(GET_ORDERS_LIST_HANDLE)
                .then();
    }

    @Step("Получение всех заказов")
    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getSpec())
                .when()
                .get(GET_ALL_ORDERS_HANDLE)
                .then();
    }
}
