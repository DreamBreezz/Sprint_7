package scooter;


import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import scooter.jsons.courier.Courier;

import static scooter.steps.CourierLoginNegativeSteps.*;
import static scooter.steps.CourierSteps.*;

public class CourierLoginTests {

    public int id;
    public Courier courier;

    @After
    public void deleteCourier() {
        deleteCourierIfExists();
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
        tryLoginNoLogin(courier);
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
}