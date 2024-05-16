package scooter;

import io.restassured.http.ContentType;
import org.junit.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertNotNull;
import static scooter.Constants.*;

public class GetOrdersListTest {

    @Test
    public void getOrdersList() {
        List<String> orders = given().log().all()
                //.config(config)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .when()
                .get(ORDERS_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("orders");

        // проверяем, что orders не пустой
        assertNotNull(orders);
    }
}