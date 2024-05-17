package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertTrue;
import static scooter.Constants.*;

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

    @Step("Создание заказа")
    public void CreateOrder() {
        var order = new Order("Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color);
        track = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .extract()
                .path("track");
    }
    @Step("Проверка, что тело ответа содержит track")
    public void checkTrack() {
        assertTrue(track > 0);
    }

    @Step("Отмена заказа, если был создан")
    public void orderCancelIfWasCreated() {
        if (track > 0) {
            String deleteOrder = "{\"track\":" + track +"}";
            boolean ok = given().log().all()
                    .contentType(ContentType.JSON)
                    .baseUri(BASE_URI)
                    .body(deleteOrder)
                    .when()
                    .put(CANCEL_ORDER_PATH)
                    .then().log().all()
                    .assertThat()
                    .statusCode(HTTP_OK)
                    .and()
                    .extract()
                    .path("ok").equals(true);
        }
    }
}
