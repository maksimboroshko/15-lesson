import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class RegistrationTests {

    private static final String URL = "https://reqres.in/api/register";
    private static final String URL_23 = "https://reqres.in/api/users/23";
    private static final String UNKNOWN_URL_23 = "https://reqres.in/api/unknown/23";
    private static final String TOKEN = "QpwL5tke4Pnpja7X4";
    private static final String EMAIL = "{\"email\": \"sydney@fife\"}";
    private static final String DATA = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

    @Test
    void successfulRegistrationTest() {
        given()
                .body(DATA)
                .contentType(ContentType.JSON)
                .log().uri()
                .when()
                .post(URL)
                .then()
                .log().status()
                .log().body()
                .body("id", is(4))
                .body("token", is(TOKEN));
    }

    @Test
    void unSuccessfulRegistrationTest() {
        given()
                .body(EMAIL)
                .contentType(ContentType.JSON)
                .log().uri()
                .when()
                .post(URL)
                .then()
                .log().status()
                .log().body()
                .body("error", is("Missing password"));
    }

    @Test
    void userNotFound415Test() {
        given()
                .log().uri()
                .when()
                .post(URL_23)
                .then()
                .log().status()
                .log().body()
                .statusCode(415);
    }

    @Test
    void userNotFoundTest() {
        given()
                .log().uri()
                .when()
                .get(URL_23)
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(is("{}"));
    }

    @Test
    void resourceNotFoundTest() {
        given()
                .log().uri()
                .when()
                .get(UNKNOWN_URL_23)
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(is("{}"));
    }
}

