package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import scooter.courier.Courier;
import scooter.login.CourierLogin;
import scooter.login.CourierLoginNoLogin;
import scooter.login.CourierLoginNoPassword;
import scooter.login.CourierLoginWrongPassword;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scooter.Constants.*;
import static scooter.rests.CourierRests.*;
import static scooter.rests.CourierLoginRests.*;

public class CourierLoginTests {

    public int id;
    public Courier courier;
    public CourierLogin courierLogin;
    public String message;
    public boolean create;

    @After
    @Step("Удаление курьера, если был создан")
    public void deleteCourier() {
        if(id > 0) {
            boolean delete = deleteCourierRest(id)
                    .assertThat().statusCode(HTTP_OK)
                    .extract().path("ok");
                    assertTrue(delete);
        }
    }

    @Test
    @DisplayName("[+] Courier - Логин курьера в системе")
    public void loginCourierTest() {
        createCourier();
        loginCourier();
        isIdReturned();
    }

    // этот тест падает по таймауту (1 мин)
    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: без пароля")
    public void loginWithoutPasswordTest() {
        createCourier();
        loginCourier();
        tryLoginWithoutPassword();
        checkErrorMessageTextNotEnoughDataToLogin();
    }

    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: без логина")
    public void loginWithoutLoginTest() {
        createCourier();
        loginCourier();
        tryLoginNoLogin();
        checkErrorMessageTextNotEnoughDataToLogin();
    }

    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: некорректный логин")
    public void loginWithWrongLoginTest() {
        tryLoginWithWrongLogin();
        checkErrorMessageTextAccountNotFound();
    }

    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: корректный логин, некорректный пароль")
    public void loginWithWrongPasswordTest() {
        createCourier();
        loginCourier();
        tryLoginWithWrongPassword();
        checkErrorMessageTextAccountNotFound();
    }



    // === S T E P S ===

    @Step("Создание курьера")
    public void createCourier() {
        // генерируем json для создания
        courier = Courier.random();
        // дёргаем ручку создания
        create = createCourierRest(courier)
                .assertThat().statusCode(HTTP_CREATED)
                .extract().path("ok");
    }

    @Step("Логин курьера")
    public void loginCourier() {
        // создаём json для логина
        courierLogin = CourierLogin.from(courier);
        // дёргаем ручку логина, чтобы узнать ID, чтобы потом удалить курьера
        id = courierLoginRest(courierLogin)
                .assertThat().statusCode(HTTP_OK)
                .extract().path("id");
    }

    @Step("Успешный запрос вернул ID")
    public void isIdReturned() {
        assert id > 0;
    }

    @Step("Попытка логина без пароля")
    public void tryLoginWithoutPassword() {
        // создаём json для логина
        CourierLoginNoPassword courier = CourierLoginNoPassword.from(courierLogin);
        // дёргаем ручку
        message = loginNoPasswordRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageTextNotEnoughDataToLogin() {
        assertEquals(message, NOT_ENOUGH_DATA_TO_LOGIN);
    }

    @Step("Попытка создания курьера без логина")
    public void tryLoginNoLogin() {
        // создаём json для логина
        CourierLoginNoLogin courier = CourierLoginNoLogin.from(courierLogin);

        // дёргаем ручку
        message = loginNoLoginRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Попытка логина с некорректным логином")
    public void tryLoginWithWrongLogin() {
        // создание json для алогина
        CourierLogin courierWrongLogin = CourierLogin.random();
        // дёргаем ручку
        message = courierLoginRest(courierWrongLogin)
                .assertThat().statusCode(HTTP_NOT_FOUND)
                .extract().path("message");
    }

    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageTextAccountNotFound() {
        assertEquals(message, ACCOUNT_NOT_FOUND);
    }

    @Step("Логин с некорректным паролем")
    public void tryLoginWithWrongPassword() {
        // создание json для такого же логина, но c некорректным паролем
        CourierLoginWrongPassword courierWrongPassword = CourierLoginWrongPassword.from(courierLogin);
        // дёргаем ручку
        message = loginWrongPasswordRest(courierWrongPassword)
                .assertThat().statusCode(HTTP_NOT_FOUND)
                .extract().path("message");
    }
}