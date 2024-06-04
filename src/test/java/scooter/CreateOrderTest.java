package scooter;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static scooter.steps.OrderSteps.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final List<String> color;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @After
    public void cancelOrder() {
        orderCancelIfWasCreated();
    }

    @Parameterized.Parameters
    public static Object[][] color() {
        return new Object[][] {
                {List.of("BLACK")},
                {List.of("GRAY")},
                {List.of("BLACK", "GRAY")},
                {List.of()},
        };
    }

    // все тесты попадают, т.к. не работает отмена заказа и нельзя вернуть систему в исходное состояние
    @Test
    @DisplayName("[+] Orders - Создание заказа: 4 разных варианта цвета")
    public void createOrderTest() {
        createOrder();
        checkTrack();
    }
}