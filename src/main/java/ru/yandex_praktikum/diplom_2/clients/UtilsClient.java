package ru.yandex_praktikum.diplom_2.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UtilsClient extends BaseClient {

    public static final String GET_INGREDIENTS_HANDLE = "/api/ingredients";
    @Step("Получение списка доступных ингридиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getSpec())
                .when()
                .get(GET_INGREDIENTS_HANDLE)
                .then();
    }
}
