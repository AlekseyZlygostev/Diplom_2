package serialsclasses;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class AfterBeforeTests {
    protected UserSerials user;
    protected ApiEndPoints api = new ApiEndPoints();
    protected String email;
    protected String password;
    protected String name;
    protected String logOutEmail;
    protected String logOutPassword;
    protected String logOutName;
    protected List<String> ingredients = new ArrayList<>();
    protected Response response;
    protected String token = "";

    @Before
    public void setUp() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().firstName();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @After
    public void clearAll(){
        try {
            token = getToken(sendLoginRequest(logOutEmail, logOutPassword)).replaceFirst("Bearer ", "");

            given()
                    .auth().oauth2(token)
                    .delete(api.getChangeUser());
        } catch (Exception exception) {
            token = "";
        }
    }

    @Step("Send POST registration request")
    public Response sendRegistrationRequest(String email, String password, String name){
        user = new UserSerials(email, password, name);
        logOutEmail = email;
        logOutPassword = password;

        response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(api.getCreateUser());
        return response;
    }

    @Step("Send POST login request ")
    public Response sendLoginRequest(String email, String password){
        user = new UserSerials(email, password);
        response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(api.getLoginUser());
        return response;
    }

    @Step("Send PATCH request")
    public Response sendPatchRequest(String token, String email, String password, String name){
        user = new UserSerials(email, password, name);
        logOutEmail = email;
        logOutPassword = password;

        response = given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(api.getChangeUser());
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

    @Step ("Set ingredients")
    public void setIngridients(){
        ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
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
}
