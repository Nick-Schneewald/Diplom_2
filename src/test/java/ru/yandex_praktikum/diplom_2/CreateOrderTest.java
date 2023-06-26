package ru.yandex_praktikum.diplom_2;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
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
import ru.yandex_praktikum.diplom_2.dataproviders.*;
import ru.yandex_praktikum.diplom_2.pojo.*;

import java.util.List;

import static ru.yandex_praktikum.diplom_2.dataproviders.OrderProvider.getRandomCreateOrderRequest;

public class CreateOrderTest {

    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private String bearerToken;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Step("Вспомогательная функция для проверки наличия заказа в списке всех заказов по параметру number")
    public boolean isOrderInAllOrdersList(List<OrdersData> orders, int number) {
        boolean flgNumberFound = false;
        for (OrdersData order :
                orders) {
            if (order.getNumber() == number) {
                flgNumberFound = true;
                break;
            }
        }
        return flgNumberFound;
    }

    @Test
    @DisplayName("Заказ не должен создаваться без авторизации")
    @Description("Проверяем, что сервис принимает заказ, но не помещает его в обшую очередь заказов")
    public void orderShouldNotBePlacedWithoutAuth() {
        CreateOrderRequest createOrderRequest = getRandomCreateOrderRequest();
        //создать заказ
        int number = orderClient.create(createOrderRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("order.number");

        //получить список всех заказов / проверить, что номера нет
        OrdersListClient ordersListClient = new OrdersListClient();
        List<OrdersData> orders = ordersListClient.getAllOrders()
                .extract().jsonPath().getList("orders", OrdersData.class);

        Assert.assertFalse(isOrderInAllOrdersList(orders, number));
    }

    @Test
    @DisplayName("Заказ должен создаваться с авторизацией")
    @Description("Проверяем, что сервис принимает заказ от авторизованного пользователя и помещает его в общую очередь")
    public void orderShouldBePlacedWithAuth() {
        CreateUserRequest createUserRequest = UserProvider.getRandomCreateUserRequest();
        //создание пользователя
        String accessToken = userClient.create(createUserRequest)
                .extract().jsonPath().get("accessToken");
        bearerToken = accessToken.substring(7);
        //создание заказа
        CreateOrderRequest createOrderRequest = getRandomCreateOrderRequest();
        String ordersId = orderClient.create(bearerToken, createOrderRequest)
                .statusCode(200)
                .body("success", Matchers.equalTo(true))
                .extract().jsonPath().get("order._id");

        //получение id списка заказов пользователя
        OrdersListClient ordersListClient = new OrdersListClient();
        ordersListClient.getOrdersList(bearerToken)
                .body("orders[0]._id", Matchers.equalTo(ordersId));
    }

    @Test
    @DisplayName("Выводится ошибка при размещении заказа без ингридиентов")
    @Description("Проверка, что возвращается ошибка при размещении заказа с пустым набором ингридиентов")
    public void errorShowedWhenPlaceOrderWithNoIngredients() {
        CreateOrderRequest orderRequest = OrderProvider.getEmptyCreateOrderRequest();
        orderClient.create(orderRequest)
                .statusCode(400)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Выводится ошибка сервера при размещении заказа с невалидным хэшем")
    @Description("Проверка, что сервис возвращает ошибку сервера при передаче слишком длинных хэшей ингридиентов")
    public void internalErrorShowedWhenPlaceOrderWithIllegalHashes() {
        CreateOrderRequest orderRequest = OrderProvider.getCreateOrderRequestWithWrongHashes();
        orderClient.create(orderRequest)
                .statusCode(500)
                .statusLine("HTTP/1.1 500 Internal Server Error");
    }

    @Test
    @DisplayName("Выводится ошибка 400 Bad Request при размещении заказа ")
    @Description("Проверка, что сервис возвращает ошибку при передаче валидных хэшей, но которых нет в списке ингридиентов")
    public void badRequestErrorShowedWhenPlaceOrderWithHashesOutOfIngredientsList() {
        CreateOrderRequest orderRequest = OrderProvider.getCreateOrderRequestWithPredefinedHashes();
        orderClient.create(orderRequest)
                .statusCode(400)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("One or more ids provided are incorrect"));
    }

    @After
    public void tearDown() {
        if (bearerToken != null) {
            userClient.delete(bearerToken);
        }
    }
}
