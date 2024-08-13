package apitestscenarios;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import serialsclasses.ApiEndPoints;
import serialsclasses.UserSerials;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderWithPostApiOrdersTest {

    UserSerials user;
    ApiEndPoints api = new ApiEndPoints();
    String email;
    String password;
    String name;
    List<String> ingredients;
    Response response;
    String token = "";

    @Before
    public void setUp() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().firstName();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        response = sendRequest(email, password, name);
        token = getToken(response).replaceFirst("Bearer ", "");
    }

    @Step("Send POST request")
    public Response sendRequest(String email, String password, String name){
        user = new UserSerials(email, password, name);

        response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(api.getCreateUser());
        return response;
    }

    @Step("Send POST order request")
    public Response sendPostOrderRequest(String token, List<String> ingredients){
        user = new UserSerials(ingredients);

        response = given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .post(api.getCreateOrder());
        return response;
    }

    @Step ("Check Status")
    public void checkStatus(Response response, boolean responseBody, String status){
        response.then().assertThat()
                .body("success", equalTo(responseBody))
                .and()
                .statusLine(containsString(status));
    }

    @Step ("Check Status")
    public void checkStatus(Response response, String status){
        response.then().assertThat()
                .statusLine(containsString(status));
    }

    @Step ("Get Token")
    public String getToken(Response response) {
        return response.then().extract().body().path("accessToken");
    }

    @Test
    public void canCreateOrderWithAuthorisation(){
        ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        response = sendPostOrderRequest(token, ingredients);
        checkStatus (response, true, "200 OK");
    }

    @Test
    public void canCreateOrderWithoutAuthorisation(){
        ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        response = sendPostOrderRequest("", ingredients);
        checkStatus (response, true, "200 OK");
    }

    @Test
    public void cannotCreateOrderWithoutIngredients(){
        response = sendPostOrderRequest(token, Collections.singletonList("1234567890"));
        checkStatus (response, "500 Internal Server Error");
    }

    @Test
    public void cannotCreateOrderWithWrongIngredientsHash(){
        ingredients = new ArrayList<>();
        response = sendPostOrderRequest("", ingredients);
        checkStatus (response, false, "400 Bad Request");
    }

    @After
    public void clearAll(){
        try {
            given()
                    .auth().oauth2(token)
                    .delete(api.getChangeUser());
        } catch (Exception exception) {
            token = "";
        }
    }
}
