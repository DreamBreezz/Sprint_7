package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import static scooter.steps.CourierCreateNegativeSteps.*;
import static scooter.steps.CourierSteps.*;

public class CourierCreateTests {

    @After
    @Step("Удаление курьера, если был создан")
    public void deleteCourier() {
        deleteCourierIfExists();
    }

    @Test
    @DisplayName("[+] Courier - Создание курьера")
    public void createCourierTest() {
        createCourier();
        checkKeyOkEqualsTrue();
    }

    // Этот тест упадёт, т.к. текст ответа не соответствует документации
    @Test
    @DisplayName("[-] Courier - Создание курьера: два одинаковых")
    public void tryCreateTwoSameCouriersTest() {
        createCourier();
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
    public void tryCreateCourierWithoutPasswordTest() {
        tryCreateCourierWithoutPassword();
        checkErrorMessageTextNotEnoughDataToCreate();
    }

    // этот тест упадёт, т.к. система позволяет создать курьера без имени
    @Test
    @DisplayName("[-] Courier - Создание курьера: без имени")
    public void tryCreateCourierWithoutNameTest() {
        tryCreateCourierWithoutName();
        checkErrorMessageTextNotEnoughDataToCreate();
    }

    // этот тест упадёт, т.к. текст ответа не соответствует документации
    @Test
    @DisplayName("[-] Courier - Создание курьера: с существующим логином")
    public void createCourierWithLoginExistsTest() {
        createCourier();
        tryCreateCourierWithSameLogin();
        checkErrorMessageTextLoginAlreadyUsed();
    }
}