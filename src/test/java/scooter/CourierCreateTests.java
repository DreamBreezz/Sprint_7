package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import scooter.courier.Courier;
import scooter.courier.CourierNoLogin;
import scooter.courier.CourierNoName;
import scooter.courier.CourierNoPassword;
import scooter.login.CourierLogin;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scooter.Constants.*;
import static scooter.rests.CourierRests.*;

public class CourierCreateTests {

    public int id;
    public boolean create;
    public Courier courier;
    public String message;

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


    // === S T E P S === //

    @Step("Создание курьера")
    public void createCourier() {
        // генерируем рандомного курьера
        courier = Courier.random();
        // дёргаем ручку создания
        create = createCourierRest(courier)
                .assertThat().statusCode(HTTP_CREATED)
                .extract().path("ok");
    }

    @Step("Проверка, что в ответе ok = true")
    public void checkKeyOkEqualsTrue() {
        assertTrue(create);
    }

    @Step("Логин курьера (для дальнейшего удаления)")
    public void courierLogin() {
        // создаём json для логина
        CourierLogin courierLogin = CourierLogin.from(courier);
        // дёргаем ручку логина, чтобы узнать ID, чтобы потом удалить курьера
        id = courierLoginRest(courierLogin)
                .assertThat().statusCode(HTTP_OK)
                .extract().path("id");
    }

    @Step("Попытка создать второго курьера с такими же параметрами, как у первого")
    public void tryCreateSecondCourier() {
        message = createCourierRest(courier)
                .assertThat().statusCode(HTTP_CONFLICT)
                .extract().path("message");
    }
    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageTextLoginAlreadyUsed() {
            assertEquals(message, LOGIN_ALREADY_USED);
    }

    @Step("Попытка создать курьера без логина")
    public void tryCreateCourierWithoutLogin() {
        CourierNoLogin courierNoLogin = CourierNoLogin.randomNoLogin();
        message = courierNoLoginRest(courierNoLogin)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageTextNotEnoughDataToCreate() {
        assertEquals(message, NOT_ENOUGH_DATA_TO_CREATE);
    }

    @Step("Попытка создать курьера без пароля")
    public void tryCreateCourierWithoutPassword() {
        CourierNoPassword courier = CourierNoPassword.randomNoPassword();
        message = courierNoPasswordRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Попытка создания курьера без имени")
    public void tryCreateCourierWithoutName() {
        CourierNoName courier = CourierNoName.randomNoName();
        message = courierNoNameRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Попытка создать второго курьера, с таким же логином")
    public void tryCreateCourierWithSameLogin() {
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
}