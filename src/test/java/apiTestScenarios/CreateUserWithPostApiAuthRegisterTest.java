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
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserWithPostApiAuthRegisterTest {
    UserSerials user;
    ApiEndPoints api = new ApiEndPoints();
    String email;
    String password;
    String name;
    Response response;
    String token = "";

    @Before
    public void setUp() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().firstName();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Step ("Send POST request")
    public Response sendRequest(String email, String password, String name){
        user = new UserSerials(email, password, name);
        response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(api.getCreateUser());
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
    public String getToken(Response response){
        return response.then().extract().body().path("accessToken");
    }

    @Test
    public void canCreateUser(){
        response = sendRequest(email, password, name);

        checkStatus (response, true, "200 OK");
    }

    @Test
    public void cannotCreateTwoIdenticalUsers(){
        response = sendRequest(email, password, name);
        Response response1 = sendRequest(email, password, name);

        checkStatus (response1, false, "403 Forbidden");

    }

    @Test
    public void cannotCreateUserWithoutEmail(){
        response = sendRequest("", password, name);

        checkStatus (response, false, "403 Forbidden");
    }

    @Test
    public void cannotCreateUserWithoutPassword(){
        response = sendRequest(email, "", name);

        checkStatus (response, false,"403 Forbidden");
    }

    @Test
    public void cannotCreateUserWithoutName(){
        response = sendRequest(email, password, "");

        checkStatus (response, false,"403 Forbidden");
    }

    @After
    public void clearAll(){
        try {
            token = getToken(response).replaceFirst("Bearer ", "");
            given()
                    .auth().oauth2(token)
                    .delete(api.getChangeUser());
        } catch (Exception exception) {
            token = "";
        }
    }

}
