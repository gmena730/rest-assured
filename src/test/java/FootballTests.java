import config.FootballConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class FootballTests extends FootballConfig {

    //Query Parameters
    @Test
    public void getDetailsOFOneArea() {
        given()
                .queryParam("areas", 2000)
        .when()
                .get("areas")
        .then();
    }

    @Test
    public void getDetailsOfMultipleAreas() {
        String areaIds = "2000,2004,2010";

        given()
                .urlEncodingEnabled(false)
                .queryParam("areas", areaIds)
        .when()
                .get("areas")
        .then();
    }

    //Assert the body of an HTTP Response
    @Test
    public void getDateFounded() {
        given()
        .when()
                .get("teams/57")
        .then()
                .body("founded", equalTo(1886));
    }

    @Test
    public void getFirstTeamName() {
        given()
        .when()
                .get("competitions/2021/teams")
        .then()
                .body("teams.name[0]", equalTo("Arsenal FC"));
    }

    //Extract the body of an HTTP Response
    @Test
    public void getAllTeamData() {
        String responseBody = get("teams/57").asString();
        System.out.println(responseBody);

    }

    //Extract and check the body of an HTTP Response
    @Test
    public void getAllTeamDara_DoCheckFirst() {
        Response response =
                given()
                .when()
                        .get("teams/57")
                .then()
                        .contentType(ContentType.JSON)
                        .extract().response();

        String jsonResponseAsString = response.asString();
        System.out.println(jsonResponseAsString);
    }

    //Extract the headers of an HTTP Response
    @Test
    public void extractHeader() {
        Response response =
            get("teams/57")
                .then()
                .extract().response();

        String contentTypeHeader = response.getContentType();
        System.out.println(contentTypeHeader);

        String apiVersionHeader = response.getHeader("X-API-Version");
        System.out.println(apiVersionHeader);
    }

    //Extract explicit data from the body with JSON path
    @Test
    public void extractFirstTeamName() {
        String firstTeamName =
                get("competitions/2021/teams").jsonPath().getString("teams.name[0]");
                System.out.println(firstTeamName);
    }

    @Test
    public void extractAllTheTeamNames() {
        Response response =
                get("competitions/2021/teams")
                        .then()
                        .extract().response();

        List<String> teamNames = response.path("teams.name");

/*
        for(String teamName : teamNames){
        System.out.println(teamName);
        }
*/

        for(int i = 0; i < teamNames.size(); i++){
            String teamName = teamNames.get(i);
            System.out.println(teamName);
        }
    }
}
