package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import static scooter.steps.CourierCreateSteps.*;

public class CourierCreateTests {

    @After
    @Step("Удаление курьера, если был создан")
    public void deleteCourier() {
        deleteCourierIfExists(id);
    }

    @Test
    @DisplayName("[+] Courier - Создание курьера")
    public void createCourierTest() {
        createCourier();
        checkKeyOkEqualsTrue();
        courierLogin();
    }

    // Этот тест упадёт, т.к. текст ответа не соответствует документации
    @Test
    @DisplayName("[-] Courier - Создание курьера: два одинаковых")
    public void tryCreateTwoSameCouriersTest() {
        createCourier();
        courierLogin();
        tryCreateSecondCourier();
        checkErrorMessageTextLoginAlreadyUsed();
    }

    @Test
    @DisplayName("[-] Courier - Создание курьера: без логина")
    public void tryCreateCourierWithoutLoginTest() {
        tryCreateCourierWithoutLogin();
        checkErrorMessageTextNotEnoughDataToCreate();
    }

    @Test
    @DisplayName("[-] Courier - Создание курьера: без пароля")
    public void createCourierWithoutPasswordTest() {
        tryCreateCourierWithoutPassword();
        checkErrorMessageTextNotEnoughDataToCreate();
    }

    // этот тест упадёт, т.к. система позволяет создать курьера без имени
    @Test
    @DisplayName("[-] Courier - Создание курьера: без имени")
    public void createCourierWithoutNameTest() {
        tryCreateCourierWithoutName();
        checkErrorMessageTextNotEnoughDataToCreate();
    }

    // этот тест упадёт, т.к. текст ответа не соответствует документации
    @Test
    @DisplayName("[-] Courier - Создание курьера: с существующим логином")
    public void createCourierWithLoginExistsTest() {
        createCourier();
        courierLogin();
        tryCreateCourierWithSameLogin();
        checkErrorMessageTextLoginAlreadyUsed();
    }
}