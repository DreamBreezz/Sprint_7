package scooter.steps;

import io.qameta.allure.Step;
import scooter.jsons.courier.Courier;
import scooter.jsons.courier.CourierNoLogin;
import scooter.jsons.courier.CourierNoName;
import scooter.jsons.courier.CourierNoPassword;
import scooter.jsons.CourierLogin;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scooter.Constants.LOGIN_ALREADY_USED;
import static scooter.Constants.NOT_ENOUGH_DATA_TO_CREATE;
import static scooter.rests.CourierRests.*;
import static scooter.rests.CourierRests.createCourierRest;

public class CourierCreateSteps {

    public static int id;
    public static boolean create;
    public static Courier courier;
    public static String message;

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
    public static void courierLogin() {
        // создаём json для логина
        CourierLogin courierLogin = CourierLogin.from(courier);
        // дёргаем ручку логина, чтобы узнать ID, чтобы потом удалить курьера
        id = courierLoginRest(courierLogin)
                .assertThat().statusCode(HTTP_OK)
                .extract().path("id");
    }

    @Step("Попытка создать второго курьера с такими же параметрами, как у первого")
    public static void tryCreateSecondCourier() {
        message = createCourierRest(courier)
                .assertThat().statusCode(HTTP_CONFLICT)
                .extract().path("message");
    }
    @Step("Проверка текста сообщения об ошибке")
    public static void checkErrorMessageTextLoginAlreadyUsed() {
        assertEquals(message, LOGIN_ALREADY_USED);
    }

    @Step("Попытка создать курьера без логина")
    public static void tryCreateCourierWithoutLogin() {
        CourierNoLogin courierNoLogin = CourierNoLogin.randomNoLogin();
        message = courierNoLoginRest(courierNoLogin)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Проверка текста сообщения об ошибке")
    public static void checkErrorMessageTextNotEnoughDataToCreate() {
        assertEquals(message, NOT_ENOUGH_DATA_TO_CREATE);
    }

    @Step("Попытка создать курьера без пароля")
    public static void tryCreateCourierWithoutPassword() {
        CourierNoPassword courier = CourierNoPassword.randomNoPassword();
        message = courierNoPasswordRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Попытка создания курьера без имени")
    public static void tryCreateCourierWithoutName() {
        CourierNoName courier = CourierNoName.randomNoName();
        message = courierNoNameRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Попытка создать второго курьера, с таким же логином")
    public static void tryCreateCourierWithSameLogin() {
        // генерируем второй json, с таким же логином, как у первого, но другим именем
        Courier sameCourierDiffName = new Courier(
                courier.getLogin(),
                courier.getPassword(),
                Courier.random().getFirstName()
        );
        // проверяем, что не удастся создать и вытаскиваем текст ошибки
        message = createCourierRest(sameCourierDiffName)
                .assertThat().statusCode(HTTP_CONFLICT)
                .extract().path("message");
    }

    @Step("Удаление курьера, если был создан")
    public static void deleteCourierIfExists(int id) {
        if(id > 0) {
            boolean delete = deleteCourierRest(id)
                    .assertThat().statusCode(HTTP_OK)
                    .extract().path("ok");
            assertTrue(delete);
        }
    }
}
