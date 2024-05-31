package scooter.steps;

import io.qameta.allure.Step;
import scooter.jsons.courier.Courier;
import scooter.jsons.login.CourierLogin;
import scooter.jsons.login.CourierLoginNoLogin;
import scooter.jsons.login.CourierLoginNoPassword;
import scooter.jsons.login.CourierLoginWrongPassword;

import static java.net.HttpURLConnection.*;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static scooter.Constants.ACCOUNT_NOT_FOUND;
import static scooter.Constants.NOT_ENOUGH_DATA_TO_LOGIN;
import static scooter.rests.CourierLoginRests.*;
import static scooter.rests.CourierRests.*;

public class CourierLoginNegativeSteps {

    public static int id;
    public static Courier courier;
    public static CourierLogin courierLogin;
    public static String message;
    public static boolean create;

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
    public static void tryLoginNoLogin(Courier courier) {
        // создаём json для логина
        CourierLoginNoLogin courierNoLogin = CourierLoginNoLogin.from(courier);

        // дёргаем ручку
        message = loginNoLoginRest(courierNoLogin)
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
