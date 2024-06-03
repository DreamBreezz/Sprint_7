package scooter;


import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static scooter.steps.CourierLoginNegativeSteps.*;
import static scooter.steps.CourierSteps.*;

public class CourierLoginTests {

    @Before
    public void createAndLoginCourier() {
        createCourier();
    }

    @After
    public void deleteCourier() {
        deleteCourierIfExists();
        courier = null;
    }

    @Test
    @DisplayName("[+] Courier - Логин курьера в системе")
    public void loginCourierTest() {
        loginCourier();
        isIdReturned();
    }

    // этот тест падает по таймауту (1 мин)
    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: без пароля")
    public void loginWithoutPasswordTest() {
        tryLoginWithoutPassword();
        checkErrorMessageTextNotEnoughDataToLogin();
    }

    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: без логина")
    public void loginWithoutLoginTest() {
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
        tryLoginWithWrongPassword();
        checkErrorMessageTextAccountNotFound();
    }
}