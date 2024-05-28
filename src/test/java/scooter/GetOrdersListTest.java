package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import java.util.List;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertNotNull;
import static scooter.rests.OrderRests.ordersGetRest;

public class GetOrdersListTest {

    public List<String> orders;

    @Test
    @DisplayName("[+] Orders - Получение списка заказов")
    public void getOrdersListTest() {
        getOrdersList();
        checkOrdersNotNull();
    }

    @Step("Получение списка заказов")
    public void getOrdersList() {
        orders = ordersGetRest()
                .assertThat().statusCode(HTTP_OK)
                .extract().path("orders");
    }

    @Step("orders не пустой")
    public void checkOrdersNotNull() {
        assertNotNull(orders);
    }
}