package scooter.steps;

import io.qameta.allure.Step;
import scooter.jsons.Order;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static scooter.rests.OrderRests.*;

public class OrderSteps {

    private static List<String> color;
    private static int track;
    private static List<String> orders;

    @Step("Создание заказа")
    public static void createOrder() {
        // создание json для заказа
        Order order = Order.defaultOrder(color);
        // тык в ручку
        track = createOrderRest(order)
                .assertThat().statusCode(HTTP_CREATED)
                .extract().path("track");
    }

    @Step("Проверка, что тело ответа содержит track")
    public static void checkTrack() {
        assertTrue(track > 0);
    }

    // всегда падает, через постман тоже
    @Step("Отмена заказа, если был создан")
    public static void orderCancelIfWasCreated() {
        if (track > 0) {
            String deleteOrder = "{\"track\":" + track +"}";
            boolean ok = orderCancelRest(deleteOrder)
                    .assertThat().statusCode(HTTP_OK)
                    .and()
                    .extract().path("ok");
            assertTrue(ok);
            track = 0;
        }
    }

    @Step("Получение списка заказов")
    public static void getOrdersList() {
        orders = ordersGetRest()
                .assertThat().statusCode(HTTP_OK)
                .extract().path("orders");
    }

    @Step("orders не пустой")
    public static void checkOrdersNotNull() {
        assertNotNull(orders);
    }
}
