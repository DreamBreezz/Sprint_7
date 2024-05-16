package scooter;

public class Constants {
    // адреса запросов
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru"; // базовый адрес для запросов
    public static final String API_PREFIX = "/api/v1"; // префикс АПИ
    public static final String CREATE_COURIER_PATH = API_PREFIX +"/courier"; // создание курьера; добавить /:id для удаления
    public static final String COURIER_LOGIN_PATH = API_PREFIX + "/courier/login"; // логин курьера
    public static final String ORDERS_PATH = API_PREFIX + "/orders"; // создание заказа, получение списка заказов
    public static final String CANCEL_ORDER_PATH = API_PREFIX + "/orders/cancel"; // отмена заказа


    // тексты ошибок
    public static final String LOGIN_ALREADY_USED = "Этот логин уже используется";
    public static final String NOT_ENOUGH_DATA_TO_CREATE = "Недостаточно данных для создания учетной записи";
    public static final String NOT_ENOUGH_DATA_TO_LOGIN = "Недостаточно данных для входа";
    public static final String ACCOUNT_NOT_FOUND = "Учетная запись не найдена";

}
