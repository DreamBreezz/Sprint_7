package scooter.steps;

import io.qameta.allure.Step;
import scooter.courier.Courier;
import scooter.login.CourierLogin;
import scooter.login.CourierLoginNoLogin;
import scooter.login.CourierLoginNoPassword;
import scooter.login.CourierLoginWrongPassword;

import static java.net.HttpURLConnection.*;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scooter.Constants.ACCOUNT_NOT_FOUND;
import static scooter.Constants.NOT_ENOUGH_DATA_TO_LOGIN;
import static scooter.rests.CourierLoginRests.*;
import static scooter.rests.CourierRests.*;

public class CourierLoginSteps {

    public static int id;
    public static Courier courier;
    public static CourierLogin courierLogin;
    public static String message;
    public static boolean create;

    @Step("Создание курьера")
    public static void createCourier() {
        // генерируем json для создания
        courier = Courier.random();
        // дёргаем ручку создания
        create = createCourierRest(courier)
                .assertThat().statusCode(HTTP_CREATED)
                .extract().path("ok");
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

    @Step("Удаление курьера, если был создан")
    public static void deleteCourierIfExists() {
        if(id > 0) {
            boolean delete = deleteCourierRest(id)
                    .assertThat().statusCode(HTTP_OK)
                    .extract().path("ok");
            assertTrue(delete);
        }
    }


    @Step("Успешный запрос вернул ID")
    public static void isIdReturned() {
        assert id > 0;
    }

    @Step("Попытка логина без пароля")
    public static void tryLoginWithoutPassword() {
        // создаём json для логина
        CourierLoginNoPassword courier = CourierLoginNoPassword.from(courierLogin);
        // дёргаем ручку
        message = loginNoPasswordRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Проверка текста сообщения об ошибке")
    public static void checkErrorMessageTextNotEnoughDataToLogin() {
        assertEquals(message, NOT_ENOUGH_DATA_TO_LOGIN);
    }

    @Step("Попытка создания курьера без логина")
    public static void tryLoginNoLogin() {
        // создаём json для логина
        CourierLoginNoLogin courier = CourierLoginNoLogin.from(courierLogin);

        // дёргаем ручку
        message = loginNoLoginRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Попытка логина с некорректным логином")
    public static void tryLoginWithWrongLogin() {
        // создание json для алогина
        CourierLogin courierWrongLogin = CourierLogin.random();
        // дёргаем ручку
        message = courierLoginRest(courierWrongLogin)
                .assertThat().statusCode(HTTP_NOT_FOUND)
                .extract().path("message");
    }

    @Step("Проверка текста сообщения об ошибке")
    public static void checkErrorMessageTextAccountNotFound() {
        assertEquals(message, ACCOUNT_NOT_FOUND);
    }

    @Step("Логин с некорректным паролем")
    public static void tryLoginWithWrongPassword() {
        // создание json для такого же логина, но c некорректным паролем
        CourierLoginWrongPassword courierWrongPassword = CourierLoginWrongPassword.from(courierLogin);
        // дёргаем ручку
        message = loginWrongPasswordRest(courierWrongPassword)
                .assertThat().statusCode(HTTP_NOT_FOUND)
                .extract().path("message");
    }
}
