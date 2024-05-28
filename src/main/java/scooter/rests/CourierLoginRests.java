package scooter.rests;

import io.restassured.response.ValidatableResponse;
import scooter.login.CourierLoginNoLogin;
import scooter.login.CourierLoginNoPassword;
import scooter.login.CourierLoginWrongPassword;

import static scooter.Constants.COURIER_LOGIN_PATH;
import static scooter.RestBase.spec;

public class CourierLoginRests {
    public static ValidatableResponse loginNoPasswordRest(CourierLoginNoPassword loginNoPassword) {
        return spec()
                .body(loginNoPassword)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all();
    }

    public static ValidatableResponse loginNoLoginRest(CourierLoginNoLogin loginNoLogin) {
        return spec()
                .body(loginNoLogin)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all();
    }

    public static ValidatableResponse loginWrongPasswordRest(CourierLoginWrongPassword courierWrongPassword) {
        return spec()
                .body(courierWrongPassword)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then().log().all();
    }
}