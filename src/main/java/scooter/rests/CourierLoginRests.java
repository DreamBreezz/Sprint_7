package scooter.rests;

import io.restassured.response.ValidatableResponse;
import scooter.jsons.login.CourierLoginNoLogin;
import scooter.jsons.login.CourierLoginNoPassword;
import scooter.jsons.login.CourierLoginWrongPassword;

import static scooter.Constants.COURIER_LOGIN_PATH;
import static scooter.rests.RestBase.spec;

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