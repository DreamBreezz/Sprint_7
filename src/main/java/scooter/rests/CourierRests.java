package scooter.rests;

import io.restassured.response.ValidatableResponse;
import scooter.jsons.courier.Courier;
import scooter.jsons.courier.CourierNoLogin;
import scooter.jsons.courier.CourierNoName;
import scooter.jsons.courier.CourierNoPassword;
import scooter.jsons.login.CourierLogin;

import static scooter.Constants.*;
import static scooter.rests.RestBase.spec;

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

    public static ValidatableResponse deleteCourierRest(int id) {
        String deleteCourier = "{\"id\":\"" + id + "\"}";
        return spec()
                .body(deleteCourier)
                .when()
                .delete(CREATE_COURIER_PATH + "/" + id)
                .then().log().all();
    }
}
