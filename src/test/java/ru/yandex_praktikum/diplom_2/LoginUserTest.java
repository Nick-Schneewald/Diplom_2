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

public class LoginUserTest {
    private static final int BEARER_TOKEN_START_POS = 7;
    private final UserClient userClient = new UserClient();
    private String bearerToken;
    private CreateUserRequest createUserRequest;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        createUserRequest = UserProvider.getRandomCreateUserRequest();

        String accessToken = userClient.create(createUserRequest)
                .extract().jsonPath().get("accessToken");
        bearerToken = accessToken.substring(BEARER_TOKEN_START_POS);
    }

    @Test
    @DisplayName("Логин пользователся с действительными учетными данными")
    @Description("Проверка входа пользователся с действительными почтой и паролем")
    public void userLoginWithExistingCredentials() {
        LoginUserRequest loginUserRequest = LoginUserRequest.from(createUserRequest);
        userClient.login(loginUserRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Выводится ошибка при логине с недействительными учетными данными")
    @Description("Проверка обработки входа с недйствительными почтой и паролем")
    public void errorShownWhenUserLoginWithWrongCredentials() {
        CreateUserRequest anotherCreateUserRequest = UserProvider.getRandomCreateUserRequest();
        LoginUserRequest loginUserRequest = LoginUserRequest.from(anotherCreateUserRequest);
        userClient.login(loginUserRequest)
                .statusCode(401)
                .body("message", Matchers.equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Пользователь не может войти без пароля")
    @Description("Проверка входа пользователя в сервис без указания обязательных параметров")
    public void userShouldNotLogInWithoutPassword() {
        LoginUserRequestWithoutPass loginUserRequestWithoutPass = LoginUserRequestWithoutPass.from(createUserRequest);
        userClient.login(loginUserRequestWithoutPass)
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Пользователь не может войти без почты")
    @Description("Проверка входа пользователя в сервис без указания обязательных параметров")
    public void userShouldNotLogInWithoutEmail() {
        LoginUserRequestWithoutEmail loginUserRequestWithoutEmail = LoginUserRequestWithoutEmail.from(createUserRequest);
        userClient.login(loginUserRequestWithoutEmail)
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        if (bearerToken != null) {
            userClient.delete(bearerToken);
        }
    }
}
