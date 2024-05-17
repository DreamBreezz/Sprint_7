package scooter;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scooter.Constants.*;

public class CourierLoginTests {

    public int id;
    public Courier courier;
    public String message;

    @After
    @Step("Удаление курьера, если был создан")
    public void deleteCourier() {
        if(id > 0) {
            String deleteCourier = "{\"id\":\""+ id +"\"}";
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
                    .path("ok").equals(true);
        }
    }

    @Test
    @DisplayName("[+] Courier - Логин курьера в системе")

    @Step("Создание курьера")
    public void createCourier() {
        // генерируем json для создания
        courier = new Courier("ninja 4591", "1234", "saske4579");

        // дёргаем ручку создания
        boolean create = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .extract()
                .path("ok").equals(true);
    }
    @Step("Логин курьера")
    public void loginCourier() {
        // создаём json для логина
        var courierLogin = CourierLogin.from(courier);

        // дёргаем ручку логина, чтобы узнать ID, чтобы потом удалить
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

    @Step("Успешный запрос вернул ID")
    public void isIdReturned() {
        assert id > 0;
    }

    // этот тест падает по таймауту (1 мин)
    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: без пароля")

    @Step("Попытка логина без пароля")
    public void tryLoginWithoutPassword() {
        // создаём json для логина
        String jsonNoPassword = "{\"login\":\"ninja450243\"}";

        // дёргаем ручку
        message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoPassword)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message");
    }
    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageText() {
        assertEquals(message, NOT_ENOUGH_DATA_TO_LOGIN);
    }

    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: без логина")

    @Step("Попытка создания курьера без логина")
    public void courierLoginNoLogin() {
        // создаём json для логина
        String jsonNoLogin = "{\"password\":\"1234\"}";

        // дёргаем ручку
        message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoLogin)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message");
    }
    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageText2() {
        assertEquals(message, NOT_ENOUGH_DATA_TO_LOGIN);
    }

    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: некорректный логин")

    @Step("Попытка логина с некорректным логином")
    public void tryLoginWithWrongLogin() {
        // создаём json для логина
        String jsonNoLogin = "{\"login\":\"ninja4507\", \"password\":\"1234\"}";

        // дёргаем ручку
        message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoLogin)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .extract()
                .path("message");
    }
    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageText3() {
        assertEquals(message, ACCOUNT_NOT_FOUND);
    }

    @Test
    @DisplayName("[-] Courier - Логин курьера в системе: корректный логин, некорректный пароль")

    @Step("Создание курьера") // чтобы иметь заведомо корректный логин
    public void createCourier2() {
        // генерируем json для создания
        var courier = new Courier("ninja 4652", "1234", "saske4579");

        // дёргаем ручку создания
        boolean create = given().log().all()
                //.config(config)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .extract()
                .path("ok").equals(true);

        // создаём json для логина с корректным паролем
        var courierLogin = CourierLogin.from(courier);

        // дёргаем ручку логина, чтобы узнать ID, чтобы потом удалить
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
    @Step("Логин с некорректным паролем")
    public void loginWithWrongPassword() {

        // создаём json для такого же логина, но c некорректным паролем
        var courierLoginWrongPass = new CourierLogin("ninja 4651", "wrongpass");

        // дёргаем ручку
        message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courierLoginWrongPass)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .extract()
                .path("message");
    }
    @Step("Проверка текста сообщения об ошибке")
    public void checkErrorMessageText4() {
        assertEquals(message, ACCOUNT_NOT_FOUND);
    }
}