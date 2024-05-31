package scooter.steps;

import io.qameta.allure.Step;
import scooter.jsons.courier.Courier;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static scooter.Constants.LOGIN_ALREADY_USED;
import static scooter.Constants.NOT_ENOUGH_DATA_TO_CREATE;
import static scooter.rests.CourierRests.createCourierRest;
import static scooter.steps.CourierSteps.courier;

public class CourierCreateNegativeSteps {

    public static int id;
    public static String message;

    @Step("Попытка создать второго курьера с такими же параметрами, как у первого")
    public static void tryCreateSecondCourier() {
        Courier courierSame = courier;  // второй курьер полностью копирует первого
        message = createCourierRest(courierSame)
                .assertThat().statusCode(HTTP_CONFLICT)
                .extract().path("message");

    }
    @Step("Проверка текста сообщения об ошибке")
    public static void checkErrorMessageTextLoginAlreadyUsed() {
        assertEquals(message, LOGIN_ALREADY_USED);
    }

    @Step("Попытка создать курьера без логина")
    public static void tryCreateCourierWithoutLogin() {
//        CourierNoLogin courierNoLogin = CourierNoLogin.randomNoLogin();
//        message = courierNoLoginRest(courierNoLogin)
//                .assertThat().statusCode(HTTP_BAD_REQUEST)
//                .extract().path("message");
        Courier courier = Courier.noLogin();
        message = createCourierRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Проверка текста сообщения об ошибке")
    public static void checkErrorMessageTextNotEnoughDataToCreate() {
        assertEquals(message, NOT_ENOUGH_DATA_TO_CREATE);
    }

    @Step("Попытка создать курьера без пароля")
    public static void tryCreateCourierWithoutPassword() {
        Courier courier = Courier.noPassword();
        message = createCourierRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Попытка создания курьера без имени")
    public static void tryCreateCourierWithoutName() {
        // создание рандомного курьера без имени
        Courier courier = Courier.noName();
        message = createCourierRest(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract().path("message");
    }

    @Step("Попытка создать второго курьера, с таким же логином")
    public static void tryCreateCourierWithSameLogin() {
        // генерация json второго курьера, с таким же логином, как у первого, но другим именем
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
