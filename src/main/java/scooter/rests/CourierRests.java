package scooter.rests;

import io.restassured.response.ValidatableResponse;
import scooter.courier.Courier;
import scooter.courier.CourierNoLogin;
import scooter.courier.CourierNoName;
import scooter.courier.CourierNoPassword;
import scooter.login.CourierLogin;

import static scooter.Constants.*;
import static scooter.RestBase.spec;

public class CourierRests {
    public static ValidatableResponse createCourierRest(Courier courier) {
        return spec()
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all();
    }

    public static ValidatableResponse courierLoginRest(CourierLogin courierLogin) {
        return spec()
                .body(courierLogin)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all();
    }

    public static ValidatableResponse courierNoLoginRest(CourierNoLogin courierNoLogin) {
        return spec()
                .body(courierNoLogin)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all();
    }

    public static ValidatableResponse courierNoPasswordRest(CourierNoPassword courierNoPassword) {
        return spec()
                .body(courierNoPassword)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all();
    }

    public static ValidatableResponse courierNoNameRest(CourierNoName courierNoName) {
        return spec()
                .body(courierNoName)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all();
    }

    public static ValidatableResponse deleteCourierRest(int id) {
        String deleteCourier = "{\"id\":\"" + id + "\"}";
        return spec()
                .body(deleteCourier)
                .when()
                .delete(CREATE_COURIER_PATH + "/" + id)
                .then().log().all();
    }
}
