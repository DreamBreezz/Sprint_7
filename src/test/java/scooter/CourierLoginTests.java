package scooter;


import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import static scooter.steps.CourierLoginSteps.*;

public class CourierLoginTests {

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
}