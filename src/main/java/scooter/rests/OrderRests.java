package scooter.rests;

import io.restassured.response.ValidatableResponse;
import scooter.jsons.order.Order;
import static scooter.Constants.*;
import static scooter.rests.RestBase.spec;

public class OrderRests {
    public static ValidatableResponse createOrderRest(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then().log().all();
    }

    public static ValidatableResponse orderCancelRest(String deleteOrder) {
        return spec()
                .body(deleteOrder)
                .when()
                .put(CANCEL_ORDER_PATH)
                .then().log().all();
    }

    public static ValidatableResponse ordersGetRest() {
        return spec()
                .when()
                .get(ORDERS_PATH)
                .then().log().all();
    }
}
