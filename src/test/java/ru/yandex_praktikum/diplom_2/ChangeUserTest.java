package ru.yandex_praktikum.diplom_2;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex_praktikum.diplom_2.clients.UserClient;
import ru.yandex_praktikum.diplom_2.dataproviders.UserProvider;
import ru.yandex_praktikum.diplom_2.pojo.*;

public class ChangeUserTest {
    private final UserClient userClient = new UserClient();
    private String bearerToken;
    private CreateUserRequest createUserRequest;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        createUserRequest = UserProvider.getRandomCreateUserRequest();
        String accessToken = userClient.create(createUserRequest)
                .extract().jsonPath().get("accessToken");
        bearerToken = accessToken.substring(7);
    }

    @Test
    @DisplayName("Изменение почты пользователся с токеном на предъявителя")
    @Description("Проверяем, что поле можно изменить")
    public void updateUserEmailWithBearerToken() {
        UpdateUserEmailRequest updateUserEmailRequest = UserProvider.getRandomUpdateUserEmailRequest();
        userClient.update(bearerToken, updateUserEmailRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .body("user.email", Matchers.equalTo(updateUserEmailRequest.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Изменение почты пользователя без токена на предъявителя")
    @Description("Проверяем, что поле нельзя изменить и что сервис возвразает ошибку")
    public void updateUserEmailWithoutBearerToken() {
        UpdateUserEmailRequest updateUserEmailRequest = UserProvider.getRandomUpdateUserEmailRequest();
        userClient.update(updateUserEmailRequest)
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение пароля пользователя с токеном на предъявителя")
    @Description("Проверяем, что поле можно изменить")
    public void updateUserPasswordWithBearerToken() {
        UpdateUserPassRequest updateUserPassRequest = UserProvider.getRandomUpdateUserPassRequest();
        userClient.update(bearerToken, updateUserPassRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true));

        String initialEmail = createUserRequest.getEmail();
        String updatedPassword = updateUserPassRequest.getPassword();
        LoginUserRequest loginUserRequest = new LoginUserRequest(initialEmail, updatedPassword);
        userClient.login(loginUserRequest)
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение пароля пользователя без токена на предъявителя")
    @Description("Проверяем, что поле нельзя изменить и что сервис возвразает ошибку")
    public void updateUserPasswordWithoutBearerToken() {
        UpdateUserPassRequest updateUserPassRequest = UserProvider.getRandomUpdateUserPassRequest();
        userClient.update(updateUserPassRequest)
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени пользователя с токеном на предъявителя")
    @Description("Проверяем, что поле можно изменить")
    public void updateUserNameWithBearerToken() {
        UpdateUserNameRequest updateUserNameRequest = UserProvider.getRandomUpdateUserNameRequest();
        userClient.update(bearerToken, updateUserNameRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .body("user.name", Matchers.equalTo(updateUserNameRequest.getName()));
    }

    @Test
    @DisplayName("Изменение имени пользователя с токеном на предъявителя")
    @Description("Проверяем, что поле нельзя изменить и что сервис возвразает ошибку")
    public void updateUserNameWithoutBearerToken() {
        UpdateUserNameRequest updateUserNameRequest = UserProvider.getRandomUpdateUserNameRequest();
        userClient.update(updateUserNameRequest)
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (bearerToken != null) {
            userClient.delete(bearerToken);
        }
    }
}
