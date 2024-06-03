package scooter.steps;

import io.qameta.allure.Step;
import scooter.jsons.CourierLogin;
import scooter.jsons.Courier;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertTrue;
import static scooter.rests.CourierRests.*;

public class CourierSteps {

    public static Courier courier;
    public static boolean create;
    public static int id;
    public static CourierLogin courierLogin;

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

    @Step("Логин курьера")
    public static void loginCourier() {
        // создаём json для логина
        courierLogin = CourierLogin.from(courier);
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

    @Step("Удаление курьера, если был создан")
    public static void deleteCourierIfExists() {
        if (courier != null) {
            loginCourier();  // чтобы удалить курьера, нам нужно его залогинить, чтобы узнать его id
            deleteCourier();
            courier = null;  // в конце очистить переменную
        }
    }
}