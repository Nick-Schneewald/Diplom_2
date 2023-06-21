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


public class CreateUserTest {
    private static final int BEARER_TOKEN_START_POS = 7;
    private final UserClient userClient = new UserClient();
    private String bearerToken;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @DisplayName("Пользователь может быть создан")
    @Description("Тест общего назначения для создания пользователя")
    public void userCouldBeCreated() {
        CreateUserRequest createUserRequest = UserProvider.getRandomCreateUserRequest();

        //создание
        String accessToken = userClient.create(createUserRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("accessToken");
        bearerToken = accessToken.substring(BEARER_TOKEN_START_POS);
        //логин
        LoginUserRequest loginUserRequest = LoginUserRequest.from(createUserRequest);
        userClient.login(loginUserRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Два пользователя с одинаковыми аттрибутами не могут быть созданы")
    @Description("Нельзя создать несколько пользователей с одинаковыми почтой/паролем/именем")
    public void twoUsersWithTheSameCredentialsShouldNotBeCreated() {
        CreateUserRequest createUserRequest = UserProvider.getRandomCreateUserRequest();

        String accessToken = userClient.create(createUserRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("accessToken");
        bearerToken = accessToken.substring(BEARER_TOKEN_START_POS);

        userClient.create(createUserRequest)
                .statusCode(403)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("User already exists"));
    }

    @Test
    @DisplayName("Пользователь не может быть создан без почты")
    @Description("Базовый тест на создание пользователя в отсутствии обязательных параметров")
    public void userShouldNotBeCreatedWithoutEmail() {
        CreateUserRequestWithoutEmail createUserRequestWithoutEmail = UserProvider.getRandomCreateUserRequestWithoutEmail();
        userClient.create(createUserRequestWithoutEmail)
                .statusCode(403)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Пользователь не может быть создан без пароля")
    @Description("Базовый тест на создание пользователя в отсутствии обязательных параметров")
    public void userShouldNotBeCreatedWithoutPassword() {
        CreateUserRequestWithoutPass createUserRequestWithoutPass = UserProvider.getRandomCreateUserRequestWithoutPass();
        userClient.create(createUserRequestWithoutPass)
                .statusCode(403)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Пользователь не может быть создан без имени")
    @Description("Базовый тест на создание пользователя в отсутствии обязательных параметров")
    public void userShouldNotBeCreatedWithoutName() {
        CreateUserRequestWithoutName createUserRequestWithoutName = UserProvider.getRandomCreateUserRequestWithoutName();
        userClient.create(createUserRequestWithoutName)
                .statusCode(403)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        if (bearerToken != null) {
            userClient.delete(bearerToken);
        }
    }
}
