package scooter;

import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static scooter.Constants.*;

public class CourierCreateTests {

    public int id;

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
    public void courierCreate() {
        // генерируем json для создания
        var courier = new Courier("ninja 4585", "1234", "saske4579");

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
    }

    // этот тест упадёт, т.к. текст ответа не соответствует документации
    @Test // нельзя создать двух одинаковых курьеров
    public void courierUnableCreateTwoSame() {
        // генерируем json для создания
        var courier = new Courier("ninja 4576", "1242", "saske4571");
        given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .extract()
                .path("ok")
        ;

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

        // пробуем создать клиента ещё раз и вытаскиваем сообщение об ошибке
        String message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .extract()
                .path("message")
                ;

        // проверяем текст сообщения об ошибке
        assertThat(message, equalTo(LOGIN_ALREADY_USED));
    }

    @Test // нельзя создать курьера, не указав логин
    public void courierCreateNoLogin() {
        String jsonNoLogin = "{\"password\":\"1234\", \"firstName\":\"saske4502\"}";
        String message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoLogin)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message")
                ;
        assertThat(message, equalTo(NOT_ENOUGH_DATA_TO_CREATE));

    }

    @Test // нельзя создать курьера, не указав пароль
    public void courierCreateNoPassword() {
        String jsonNoPassword = "{\"login\":\"ninja4507\", \"firstName\":\"saske4503\"}";
        String message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoPassword)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message")
                ;
        assertThat(message, equalTo(NOT_ENOUGH_DATA_TO_CREATE));
    }

    // этот тест упадёт, т.к. возможно создать курьера без имени
    @Test // нельзя создать курьера, не указав имя
    public void courierCreateNoName() {
        String jsonNoName = "{\"login\":\"ninja4588\", \"password\":\"1234\"}";
        String message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(jsonNoName)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat().statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message")
                ;
        assertThat(message, equalTo(NOT_ENOUGH_DATA_TO_CREATE));
    }

    // этот тест упадёт, т.к. текст ответа не соответствует документации
    @Test // нельзя создать курьера с существующим логином
    public void courierCreateSameLogin() {
        // генерируем json первого курьера
        var courierOne = new Courier("ninja4589", "1234", "saske4572");

        //создаём клиента
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
                .path("ok")
                ;

        //проверяем, что ok = true
        assertTrue(create);

        // создаём json для логина
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

        // генерируем второй json, с таким же логином, как у первого, но другим именем
        String json2 = "{\"login\":\"ninja4589\", \"password\":\"123434\", \"firstName\":\"saske455543\"}";

        // проверяем, что не удастся создать
        String message = given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(json2)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all()
                .assertThat().statusCode(HTTP_CONFLICT)
                .extract()
                .path("message")
                ;
        assertThat(message, equalTo(LOGIN_ALREADY_USED));
    }
}
