package ru.yandex_praktikum.diplom_2.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex_praktikum.diplom_2.pojo.*;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {
    public static final String CREATE_USER_HANDLE = "/api/auth/register";
    public static final String LOGIN_USER_HANDLE = "/api/auth/login";
    public static final String UPDATE_USER_HANDLE = "/api/auth/user";

    public static final String DELETE_USER_HANDLE = "/api/auth/user";

    @Step("Создание пользователся со всеми обязательными параметрами")
    public ValidatableResponse create(CreateUserRequest createUserRequest) {
        return given()
                .spec(getSpec())
                .body(createUserRequest)
                .when()
                .post(CREATE_USER_HANDLE)
                .then();
    }

    @Step("Создание пользователся без указания почты")
    public ValidatableResponse create(CreateUserRequestWithoutEmail createUserRequestWithoutEmail) {
        return given()
                .spec(getSpec())
                .body(createUserRequestWithoutEmail)
                .when()
                .post(CREATE_USER_HANDLE)
                .then();
    }

    @Step("Создание пользователся без указания пароля")
    public ValidatableResponse create(CreateUserRequestWithoutPass createUserRequestWithoutPass) {
        return given()
                .spec(getSpec())
                .body(createUserRequestWithoutPass)
                .when()
                .post(CREATE_USER_HANDLE)
                .then();
    }

    @Step("Создание ползователя без указания имени")
    public ValidatableResponse create(CreateUserRequestWithoutName createUserRequestWithoutName) {
        return given()
                .spec(getSpec())
                .body(createUserRequestWithoutName)
                .when()
                .post(CREATE_USER_HANDLE)
                .then();
    }

    @Step("Выполнение логина со всеми обязательными параметрами")
    public ValidatableResponse login(LoginUserRequest loginUserRequest) {
        return given()
                .spec(getSpec())
                .body(loginUserRequest)
                .when()
                .post(LOGIN_USER_HANDLE)
                .then();
    }

    @Step("Выполнение логина без указания пароля пользователя")
    public ValidatableResponse login(LoginUserRequestWithoutPass loginUserRequestWithoutPass) {
        return given()
                .spec(getSpec())
                .body(loginUserRequestWithoutPass)
                .when()
                .post(LOGIN_USER_HANDLE)
                .then();
    }

    @Step("Выполнение логина без указания почты пользователя")
    public ValidatableResponse login(LoginUserRequestWithoutEmail loginUserRequestWithoutEmail) {
        return given()
                .spec(getSpec())
                .body(loginUserRequestWithoutEmail)
                .when()
                .post(LOGIN_USER_HANDLE)
                .then();
    }

    @Step("Изменение почты пользователя с авторизацией")
    public ValidatableResponse update(String bearerToken, UpdateUserEmailRequest updateUserEmailRequest) {
        return given()
                .spec(getSpec())
                .auth().oauth2(bearerToken)
                .body(updateUserEmailRequest)
                .when()
                .patch(UPDATE_USER_HANDLE)
                .then();
    }

    @Step("Изменение почты пользователя без авторизацией")
    public ValidatableResponse update(UpdateUserEmailRequest updateUserEmailRequest) {
        return given()
                .spec(getSpec())
                .body(updateUserEmailRequest)
                .when()
                .patch(UPDATE_USER_HANDLE)
                .then();
    }

    @Step("Изменение пароля пользователя с авторизацией")
    public ValidatableResponse update(String bearerToken, UpdateUserPassRequest updateUserPassRequest) {
        return given()
                .spec(getSpec())
                .auth().oauth2(bearerToken)
                .body(updateUserPassRequest)
                .when()
                .patch(UPDATE_USER_HANDLE)
                .then();
    }

    @Step("Изменение пароля пользователя без авторизации")
    public ValidatableResponse update(UpdateUserPassRequest updateUserPassRequest) {
        return given()
                .spec(getSpec())
                .body(updateUserPassRequest)
                .when()
                .patch(UPDATE_USER_HANDLE)
                .then();
    }

    @Step("Изменение имени пользователя без авторизации")
    public ValidatableResponse update(UpdateUserNameRequest updateUserNameRequest) {
        return given()
                .spec(getSpec())
                .body(updateUserNameRequest)
                .when()
                .patch(UPDATE_USER_HANDLE)
                .then();
    }

    @Step("Изменение имени пользоватлея с авторизацией")
    public ValidatableResponse update(String bearerToken, UpdateUserNameRequest updateUserNameRequest) {
        return given()
                .spec(getSpec())
                .auth().oauth2(bearerToken)
                .body(updateUserNameRequest)
                .when()
                .patch(UPDATE_USER_HANDLE)
                .then();
    }

    @Step("Удаление пользователя")
    public void delete(String bearerToken) {
        given()
                .spec(getSpec())
                .auth().oauth2(bearerToken)
                .when()
                .delete(DELETE_USER_HANDLE);
    }
}
