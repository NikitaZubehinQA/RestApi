import models.LoginRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class BasicAuth {
    private static final String HTTPBIN_BASE_URL = "https://httpbin.org";
    private static final String BASIC_USER = "user";
    private static final String BASIC_PASS = "pass";
    private static final String LOGIN_EMAIL = "test@mail.com";
    private static final String LOGIN_PASSWORD = "1234";

    @BeforeAll
    static void setup() {
        baseURI = HTTPBIN_BASE_URL;
    }

    private static LoginRequest defaultLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail(LOGIN_EMAIL);
        request.setPassword(LOGIN_PASSWORD);
        return request;
    }

    private static String toJson(LoginRequest request) {
        return String.format(
                "{\"email\":\"%s\",\"password\":\"%s\"}",
                request.getEmail(),
                request.getPassword()
        );
    }

    private static String tokenFor(String email) {
        return email + "_token";
    }

    @Test
    void basicAuthTest() {
        given()
                .auth().basic(BASIC_USER, BASIC_PASS)
                .log().all()
                .when()
                .get("/basic-auth/" + BASIC_USER + "/" + BASIC_PASS)
                .then()
                .log().all()
                .statusCode(200)
                .body("authenticated", is(true))
                .body("user", is(BASIC_USER));
    }

    @Test
    void loginPostTest() {
        LoginRequest request = defaultLoginRequest();

        given()
                .log().all()
                .contentType(JSON)
                .body(toJson(request))
                .when()
                .post("/post")
                .then()
                .log().all()
                .statusCode(200)
                .body("json.email", is(LOGIN_EMAIL))
                .body("json.password", is(LOGIN_PASSWORD));
    }

    @Test
    void loginAndCheckTokenFlow() {
        LoginRequest request = defaultLoginRequest();

        String email =
                given()
                        .contentType(JSON)
                        .body(toJson(request))
                        .when()
                        .post("/post")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("json.email");

        String token = tokenFor(email);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/get")
                .then()
                .statusCode(200)
                .body("headers.Authorization", is("Bearer " + token));
    }
}
