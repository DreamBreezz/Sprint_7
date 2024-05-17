package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertNotNull;
import static scooter.Constants.*;

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
        orders = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .when()
                .get(ORDERS_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("orders");
    }
    @Step("orders не пустой")
    public void checkOrdersNotNull() {
        assertNotNull(orders);
    }
}