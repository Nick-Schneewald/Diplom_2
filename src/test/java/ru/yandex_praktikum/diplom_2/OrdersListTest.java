package ru.yandex_praktikum.diplom_2;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex_praktikum.diplom_2.clients.*;
import ru.yandex_praktikum.diplom_2.dataproviders.UserProvider;
import ru.yandex_praktikum.diplom_2.pojo.*;

import java.util.List;

import static ru.yandex_praktikum.diplom_2.dataproviders.OrderProvider.getRandomCreateOrderRequest;

public class OrdersListTest {
    private final OrdersListClient ordersListClient = new OrdersListClient();
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private String bearerToken;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @DisplayName("Получить список заказов пользователя без токена на предъявителя")
    @Description("Получение заказов конкретного пользователя для неавторизованного пользователя")
    public void getOrdersListWithoutBearerToken() {
        ordersListClient.getOrdersList()
                .statusCode(401)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Получить список заказов для пользователя без размещенных заказов")
    @Description("Получение заказов конкретного пользователя для авторизованного пользователя без заказов")
    public void getOrdersListOfUserWithoutOrdersPlaced() {
        CreateUserRequest createUserRequest = UserProvider.getRandomCreateUserRequest();

        String accessToken = userClient.create(createUserRequest)
                .extract().jsonPath().get("accessToken");
        bearerToken = accessToken.substring(7);

        List<OrdersData> orders = ordersListClient.getOrdersList(bearerToken)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().getList("orders", OrdersData.class);
        Assert.assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Получить список заказов для пользователя с размещенными заказами")
    @Description("Получение заказов конкретного пользователя для авторизованного пользователя с непустым списком заказов")
    public void getOrdersListOfUserWithOrdersPlaced() {
        CreateUserRequest createUserRequest = UserProvider.getRandomCreateUserRequest();
        //создание пользователя
        String accessToken = userClient.create(createUserRequest)
                .extract().jsonPath().get("accessToken");
        bearerToken = accessToken.substring(7);
        //создать заказ для пользователся/получить id
        CreateOrderRequest createOrderRequest = getRandomCreateOrderRequest();
        String ordersId = orderClient.create(bearerToken, createOrderRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("order._id");

        //получить список заказов для пользователя
        ordersListClient.getOrdersList(bearerToken)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .body("orders[0]._id", Matchers.equalTo(ordersId));
    }

    @After
    public void tearDown() {
        if (bearerToken != null) {
            userClient.delete(bearerToken);
        }
    }
}
