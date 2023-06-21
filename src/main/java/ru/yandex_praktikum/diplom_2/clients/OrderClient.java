package ru.yandex_praktikum.diplom_2.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex_praktikum.diplom_2.pojo.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private static final String PLACE_ORDER_HANDLE = "/api/orders";

    @Step("Создание заказа без авторизации")
    public ValidatableResponse create(CreateOrderRequest createOrderRequest) {
        return given()
                .spec(getSpec())
                .body(createOrderRequest)
                .when()
                .post(PLACE_ORDER_HANDLE)
                .then();
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse create(String bearerToken, CreateOrderRequest createOrderRequest) {
        return given()
                .spec(getSpec())
                .auth().oauth2(bearerToken)
                .body(createOrderRequest)
                .when()
                .post(PLACE_ORDER_HANDLE)
                .then();
    }

}
