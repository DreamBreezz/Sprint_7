package scooter;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.apache.http.params.CoreConnectionPNames;
import org.junit.After;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static scooter.Constants.*;

public class CourierLoginTests {

    public int id;

    // добавляем конфиг с таймаутом, т.к. запрос может вообще не отвечать (проверено в Постмане)
    RestAssuredConfig config = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000)
                    .setParam(CoreConnectionPNames.SO_TIMEOUT, 5000));

    @After
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
                    .path("ok");
            assertTrue(delete);
        }
    }

    @Test // создание курьера
    public void courierLogin() {
        // генерируем json для создания
        var courier = new Courier("ninja 4590", "1234", "saske4579");

        // дёргаем ручку создания
        boolean create = given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .extract()
                .path("ok")
                ;

        //проверяем, что ok = true
        assertTrue(create);

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

        //
        assert id > 0;
    }

    // этот тест падает по таймауту
    @Test // отправляем лог без пасса, получаем ошибку
    public void courierLoginNoPassword() {
        // создаём json для логина
        String jsonNoPassword = "{\"login\":\"ninja4507\"}";

        // дёргаем ручку
        String message = given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoPassword)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message")
                ;

        // проверяем текст ответа
        assertThat(message, equalTo(NOT_ENOUGH_DATA_TO_LOGIN));
    }

    @Test // отправляем пасс без логина, получаем ошибку
    public void courierLoginNoLogin() {
        // создаём json для логина
        String jsonNoLogin = "{\"password\":\"1234\"}";

        // дёргаем ручку
        String message = given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoLogin)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message")
                ;

        // проверяем текст ответа
        assertThat(message, equalTo(NOT_ENOUGH_DATA_TO_LOGIN));
    }

    @Test // отправляем некорректный логин и какой-нибудь пароль, получаем ошибку
    public void courierLoginWrongLogin() {
        // создаём json для логина
        String jsonNoLogin = "{\"login\":\"ninja4507\", \"password\":\"1234\"}";

        // дёргаем ручку
        String message = given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoLogin)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .extract()
                .path("message")
                ;

        // проверяем текст ответа
        assertThat(message, equalTo(ACCOUNT_NOT_FOUND));
    }

    @Test // отправляем корректный логин и некорректный пароль, получаем ошибку
    public void courierLoginWrongPassword() {
        // сначала создаём курьера, чтобы иметь заведомо корректный логин
        // генерируем json для создания
        var courier = new Courier("ninja 4651", "1234", "saske4579");

        // дёргаем ручку создания
        boolean create = given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .extract()
                .path("ok")
                ;

        //проверяем, что ok = true
        assertTrue(create);

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

        // создаём json для такого же логина, но c некорректным паролем
        var courierLoginWrongPass = new CourierLogin("ninja 4651", "wrongpass");

        // дёргаем ручку
        String message = given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courierLoginWrongPass)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .extract()
                .path("message")
                ;

        // проверяем текст ответа
        assertThat(message, equalTo(ACCOUNT_NOT_FOUND));
    }

}
