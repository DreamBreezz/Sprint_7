package scooter.steps;

import io.qameta.allure.Step;
import scooter.jsons.courier.CourierNoName;
import scooter.jsons.login.CourierLogin;
import scooter.jsons.courier.Courier;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertTrue;
import static scooter.rests.CourierRests.*;

public class CourierSteps {

    public static Courier courier;
    public static CourierNoName courierNoName;
    public static boolean create;
    public static int id;

    @Step("Создание курьера")
    public static void createCourier() {
        // генерируем рандомного курьера
        courier = Courier.random();
        // дёргаем ручку создания
        create = createCourierRest(courier)
                .assertThat().statusCode(HTTP_CREATED)
                .extract().path("ok");
    }

    @Step("Проверка, что в ответе ok = true")
    public static void checkKeyOkEqualsTrue() {
        assertTrue(create);
    }

    @Step("Логин курьера (для дальнейшего удаления)")
    public static void loginCourier() {
        // создаём json для логина
        CourierLogin courierLogin = CourierLogin.from(courier);
        // дёргаем ручку логина, чтобы узнать ID, чтобы потом удалить курьера
        id = courierLoginRest(courierLogin)
                .assertThat().statusCode(HTTP_OK)
                .extract().path("id");
    }

    @Step("Успешный запрос вернул ID")
    public static void isIdReturned() {
        assert id > 0;
    }

    public static void deleteCourier() {
        if(id > 0) {
            boolean delete = deleteCourierRest(id)
                    .assertThat().statusCode(HTTP_OK)
                    .extract().path("ok");
            assertTrue(delete);
            id = 0;
        }
    }

    @Step("Удаление курьера")
    public static void deleteCourierIfExists() {
        if (courier != null) {
            loginCourier();  // чтобы удалить курьера, нам нужно узнать его id
            deleteCourier();
            courier = null;  // в коце теста надо удалить курьера
        }
    }
}