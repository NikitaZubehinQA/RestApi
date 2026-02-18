import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BeforeAllTests {


    @BeforeAll
    static void setup() {
        baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    void shouldGetPostByIdWithBeforeAll() {

        given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("userId", is(1));
    }

    @Test
    void shouldReturnCorrectId() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/posts/1")
                .then()
                .statusCode(200);

    }

  @Test
    void ReturnCorrectId() {


      given()
              .when()
              .get("https://jsonplaceholder.typicode.com/posts/1")
              .then()
              .statusCode(200)
              .body("id", is(1));
  }

    @Test
    void shouldReturn100Posts() {
    given()
            .when()
            .get("/posts")
            .then()
            .statusCode(200)
            .body("size()",is(100));

    }

    @Test
    void shouldCheckFirstPostFields() {
        given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(1))
                .body("[0].userId", is(1));
    }

    @Test
    void shouldReturnPostsOnlyForUser1() {
        given()
                .queryParam("userId", 1)
                .get("/posts")
                .then()
                .statusCode(200)
                .body("userId", everyItem((equalTo(1))));

    }

    @Test
    void shouldReturnPostsOnlyForPostUser() {
        given()
                .queryParam("userId", 1)
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()",is(10))
                .body("userId", everyItem(equalTo(1)));


    }


}






