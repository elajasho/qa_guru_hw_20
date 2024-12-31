package qaguru;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresTest extends BaseTest {


    @Test
    public void getSingleUserTest() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.avatar", is("https://reqres.in/img/faces/2-image.jpg"));

                System.out.println("123");
    }

    @Test
    public void postRegisterSuccessfulTest() {
        String authBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
                .log().uri()
                .log().method()
                .body(authBody)

                .when()
                .contentType(JSON)
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void postRegisterUnsuccessfulTest() {
        String authBody = "{\"email\": \"sydney@fife\"}";

        given()
                .log().uri()
                .log().method()
                .body(authBody)
                .when()
                .contentType(JSON)
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400) //
                .body("error", is("Missing password"));
    }

    @Test
    public void postLoginSuccessfulTest() {
        String authBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .log().uri()
                .log().method()
                .body(authBody)
                .when()
                .contentType(JSON)
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200) //
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void postLoginUnsuccessfulTest() {
        String authBody = "{\"email\": \"peter@klaven\"}";

        given()
                .log().uri()
                .log().method()
                .body(authBody)
                .when()
                .contentType(JSON)
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400) //
                .body("error", is("Missing password"));
    }

    @Test
    public void getDelayedResponseTest() {
        given()
                .log().uri()
                .log().method()
                .when()
                .contentType(JSON)
                .get("/users?delay=3")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));

    }


}
