package scooter.steps;

import io.qameta.allure.Step;
import scooter.jsons.CourierLogin;
import static java.net.HttpURLConnection.*;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static scooter.Constants.ACCOUNT_NOT_FOUND;
import static scooter.Constants.NOT_ENOUGH_DATA_TO_LOGIN;
import static scooter.rests.CourierRests.*;
import static scooter.steps.CourierSteps.courier;

public class CourierLoginNegativeSteps {

    public static String message;

    @Step("Попытка логина без пароля")
    public static void tryLoginWithoutPassword() {
        // создаём json для логина
        CourierLogin c = CourierLogin.noPassword(courier);
        // дёргаем ручку
        message = courierLoginRest(c)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Проверка текста сообщения об ошибке")
    public static void checkErrorMessageTextNotEnoughDataToLogin() {
        assertEquals(message, NOT_ENOUGH_DATA_TO_LOGIN);
    }

    @Step("Попытка создания курьера без логина")
    public static void tryLoginNoLogin() {
        CourierLogin c = CourierLogin.noLogin(courier);
        message = courierLoginRest(c)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Попытка логина с некорректным логином")
    public static void tryLoginWithWrongLogin() {
        CourierLogin c = CourierLogin.wrongLogin(courier);
        message = courierLoginRest(c)
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
        CourierLogin c = CourierLogin.wrongPassword(courier);
        // дёргаем ручку
        message = courierLoginRest(c)
                .assertThat().statusCode(HTTP_NOT_FOUND)
                .extract().path("message");
    }
}
