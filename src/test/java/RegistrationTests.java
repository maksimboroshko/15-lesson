import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.Step;
import models.RegistrationRequest;
import models.RegistrationResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class RegistrationTests {
    //не работает...
    private static final String BASE_URL = "https://reqres.in/api";
    private final RequestSpecification requestSpec = given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .log().all();

    @Test
    void unSuccessfulRegistrationTest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("sydney@fife");

        executeUnsuccessfulRegistration(request, "Missing password");
    }

    @Test
    void successfulRegistrationTest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("eve.holt@reqres.in");
        request.setPassword("pistol");

        RegistrationResponse response = executeSuccessfulRegistration(request);
        validateSuccessfulRegistration(response);
    }

    @Test
    void userNotFoundTest() {
        given(requestSpec)
                .when()
                .get("/users/23")
                .then()
                .statusCode(404)
                .body(is("{}"));
    }

    @Test
    void resourceNotFoundTest() {
        given(requestSpec)
                .when()
                .get("/unknown/23")
                .then()
                .statusCode(404)
                .body(is("{}"));
    }

    @Step("Отправка запроса на неудачную регистрацию")
    private void executeUnsuccessfulRegistration(RegistrationRequest request, String expectedError) {
        given(requestSpec)
                .body(request)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body("error", is(expectedError));
    }

    @Step("Отправка запроса на успешную регистрацию")
    private RegistrationResponse executeSuccessfulRegistration(RegistrationRequest request) {
        return given(requestSpec)
                .body(request)
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .extract()
                .as(RegistrationResponse.class);
    }

    @Step("Валидация успешной регистрации")
    private void validateSuccessfulRegistration(RegistrationResponse response) {
        assert response.getId() == 4 : "ID пользователя не совпадает!";
        assert response.getToken().equals("QpwL5tke4Pnpja7X4") : "Токен не совпадает!";
    }
}
