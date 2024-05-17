package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scooter.Constants.*;

public class CourierCreateTests {

    public int id;
    public boolean create;
    public Courier courier;
    public String message;

    @After
    @Step("Удаление курьера, если был создан")
    public void deleteCourier() {
        if(id > 0) {
            String deleteCourier = "{\"id\":\"" + id + "\"}";
            boolean delete = given().log().all()
                    .contentType(ContentType.JSON)
                    .baseUri(BASE_URI)
                    .body(deleteCourier)
                    .when()
                    .delete(CREATE_COURIER_PATH +"/"+id)
                    .then().log().all()
                    .assertThat()
                    .statusCode(HTTP_OK)
                    .and()
                    .extract()
                    .path("ok");
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
        // генерируем json для создания
        courier = new Courier("ninja5026", "1234", "saske4579");
        // дёргаем ручку создания
        create = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .extract()
                .path("ok");
    }
    @Step("Проверка, что в ответе ok = true")
    public void checkKeyOkEqualsTrue() {
        assertTrue(create);
    }
    @Step("Логин курьера (для дальнейшего удаления)")
    public void courierLogin() {
        // создаём json для логина
        var courierLogin = CourierLogin.from(courier);

        // дёргаем ручку логина, чтобы узнать ID, чтобы потом удалить курьера
        id = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courierLogin)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("id");
    }

    @Step("Попытка создать второго курьера с такими же параметрами, как у первого")
    public void tryCreateSecondCourier() {
        message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .extract()
                .path("message");
    }
    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageTextLoginAlreadyUsed() {
            assertEquals(message, LOGIN_ALREADY_USED);
    }

    @Step("Попытка создать курьера без логина")
    public void tryCreateCourierWithoutLogin() {
        String jsonNoLogin = "{\"password\":\"1234\", \"firstName\":\"saske4502\"}";
        message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoLogin)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message");
    }

    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageTextNotEnoughDataToCreate() {
        assertEquals(message, NOT_ENOUGH_DATA_TO_CREATE);
    }

    @Step("Попытка создать курьера без пароля")
    public void tryCreateCourierWithoutPassword() {
        String jsonNoPassword = "{\"login\":\"ninja5012\", \"firstName\":\"saske4503\"}";
        message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoPassword)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message");
    }

    @Step("Попытка создания курьера без имени")
    public void tryCreateCourierWithoutName() {
        String jsonNoName = "{\"login\":\"ninja5013\", \"password\":\"1234\"}";
        message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoName)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message");
    }

    @Step("Создание первого курьера")
    public void createFirstCourier2() {
        // генерируем json первого курьера
        var courierOne = new Courier("ninja5014", "1234", "saske4572");

        // дёргаем ручку создания
        boolean create = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courierOne)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .extract()
                .path("ok").equals(true);

        // создаём json для логина - нам нужен ID курьера, чтобы потом его удалить
        var courierOneLogin = CourierLogin.from(courierOne);

        // дёргаем ручку логина, чтобы узнать ID, чтобы потом удалить
        id = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courierOneLogin)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("id");
    }
    @Step("Попытка создать второго курьера, с таким же логином")
    public void tryCreateCourierWithSameLogin() {
        // генерируем второй json, с таким же логином, как у первого, но другим именем
        String json2 = "{\"login\":\"ninja5026\", \"password\":\"123434\", \"firstName\":\"saske455543\"}";

        // проверяем, что не удастся создать
        message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(json2)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat().statusCode(HTTP_CONFLICT)
                .extract()
                .path("message");
        }
}