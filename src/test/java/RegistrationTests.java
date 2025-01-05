import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


public class RegistrationTests {
    String url =  "https://reqres.in/api/register";
    String url23 =  "https://reqres.in/api/users/23";
    String unknownUrl23 =  "https://reqres.in/api/unknown/23";
    String token = "QpwL5tke4Pnpja7X4";
    String email = "{\"email\": \"sydney@fife\"}";
    String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";


    @Test
    void SuccessfulRegistrationTest() {
        given()
                .body(data)
                .contentType(ContentType.JSON)
                .log().uri()
                .when()
                .post(url)
                .then()
                .log().status()
                .log().body()
                .body("id", is(4))
                .body("token", is(token));
    }
    @Test
    void UnSuccessfulRegistrationTest() {
        given()
                .body(email)
                .contentType(ContentType.JSON)
                .log().uri()
                .when()
                .post(url)
                .then()
                .log().status()
                .log().body()
                .body("error" , is("Missing password"));
    }
    @Test
    void UserNotFound415Test() {
        given()
                .log().uri()
                .when()
                .post(url23)
                .then()
                .log().status()
                .log().body()
                .statusCode(415);
    }

    @Test
    void UserNotFoundTest() {
        given()
                .log().uri()
                .when()
                .get(url23)
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(is("{}"));
    }
    @Test
    void ResourceNotFoundTest() {
        given()
                .log().uri()
                .when()
                .get(unknownUrl23)
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(is("{}"));
    }


}
