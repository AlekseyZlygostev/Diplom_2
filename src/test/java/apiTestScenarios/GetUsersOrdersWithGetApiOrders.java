package apiTestScenarios;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import serialsClasses.ApiEndPoints;
import serialsClasses.UserSerials;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetUsersOrdersWithGetApiOrders {

    UserSerials user;
    ApiEndPoints api = new ApiEndPoints();
    String email;
    String password;
    String name;
    List<String> ingredients = new ArrayList<>();
    Response response;
    String token = "";

    @Before
    public void setUp() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().firstName();
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        response = sendRequest(email, password, name);
        token = getToken(response).replaceFirst("Bearer ", "");
        sendPostOrderRequest(token, ingredients);
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

    @Step("Send GET order request")
    public Response sendGetOrderRequest(String token){
        response = given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .get(api.getCreateOrder());
        return response;
    }

    @Step ("Check Status")
    public void checkStatus(Response response, boolean responseBody, String status){
        response.then().assertThat()
                .body("success", equalTo(responseBody))
                .and()
                .statusLine(containsString(status));
    }

    @Step ("Get Token")
    public String getToken(Response response) {
        return response.then().extract().body().path("accessToken");
    }

    @Test
    public void canGetOrdersWithAuthUser(){
        response = sendGetOrderRequest(token);
        checkStatus (response, true, "200 OK");
    }

    @Test
    public void cannotGetOrdersWithoutAuthUser(){
        response = sendGetOrderRequest("");
        checkStatus (response, false, "401 Unauthorized");
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
