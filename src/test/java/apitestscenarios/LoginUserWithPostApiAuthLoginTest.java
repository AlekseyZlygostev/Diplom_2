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

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserWithPostApiAuthLoginTest {
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
        response = sendRequest(email, password, name);
    }

    @Step("Send POST request /api/auth/register")
    public Response sendRequest(String email, String password, String name){
        user = new UserSerials(email, password, name);
        response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(api.getCreateUser());
        return response;
    }

    @Step("Send POST request /api/auth/login")
    public Response sendLoginRequest(String email, String password){
        user = new UserSerials(email, password);
        response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(api.getLoginUser());
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
    public void canLoginExistUser(){
        response = sendLoginRequest(email, password);

        checkStatus (response, true, "200 OK");
    }

    @Test
    public void cannotLoginUserWithWrongEmail(){
        response = sendLoginRequest(email + "ru", password);

        checkStatus (response, false, "401 Unauthorized");
    }

    @Test
    public void cannotLoginUserWithWrongPassword(){
        response = sendLoginRequest(email, password + "123");

        checkStatus (response, false, "401 Unauthorized");
    }

    @After
    public void clearAll(){
        try {
            token = getToken(response).replaceFirst("Bearer ", "");
            response = given()
                    .auth().oauth2(token)
                    .delete(api.getChangeUser());
        } catch (Exception exception) {
            token = "";
        }
    }
}
