package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import scooter.order.Order;

import java.util.List;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertTrue;
import static scooter.rests.OrderRests.createOrderRest;
import static scooter.rests.OrderRests.orderCancelRest;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private List<String> color;
    private int track;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] color() {
        return new Object[][] {
                {List.of("BLACK")},
                {List.of("GRAY")},
                {List.of("BLACK", "GRAY")},
                {List.of()},
        };
    }

    // все тесты попадают, т.к. не работает отмена заказа и нельзя вернуть систему в исходное состояние
    @Test
    @DisplayName("[+] Orders - Создание заказа: 4 разных варианта цвета")
    public void createOrderTest() {
        createOrder();
        checkTrack();
        orderCancelIfWasCreated();
    }

    @Step("Создание заказа")
    public void createOrder() {
        // создание json для заказа
        Order order = Order.defaultOrder(color);
        // тык в ручку
        track = createOrderRest(order)
                .assertThat().statusCode(HTTP_CREATED)
                .extract().path("track");
    }

    @Step("Проверка, что тело ответа содержит track")
    public void checkTrack() {
        assertTrue(track > 0);
    }

    // всегда падает, через постман тоже
    @Step("Отмена заказа, если был создан")
    public void orderCancelIfWasCreated() {
        if (track > 0) {
            String deleteOrder = "{\"track\":" + track +"}";
            boolean ok = orderCancelRest(deleteOrder)
                    .assertThat().statusCode(HTTP_OK)
                    .and()
                    .extract().path("ok").equals(true);
        }
    }
}
