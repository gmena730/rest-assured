import config.VideoGameConfig;
import config.VideoGameEndPoints;
import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import objects.VideoGame;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.lessThan;

public class VideoGameTests extends VideoGameConfig {

    String gameBodyJSON = """
                {
                  "category": "Platform",
                  "name": "Mario",
                  "rating": "Mature",
                  "releaseDate": "2012-05-04",
                  "reviewScore": 85
                }""";

    //GET Request
    @Test
    public void getAllGames() {
        given()
        .when()
                .get(VideoGameEndPoints.ALL_VIDEO_GAMES)
        .then();
    }

    //POST Request with JSON
    @Test
    public void createNewGameByJSON() {

        given()
                .body(gameBodyJSON)
        .when()
                .post(VideoGameEndPoints.ALL_VIDEO_GAMES)
        .then();

    }

    //POST Request with XML
    @Test
    public void createNewGamebyXML() {
        String gameBodyXML = """
                <?xml version="1.0" encoding="UTF-8"?>
                <VideoGameRequest>
                \t<category>Platform</category>
                \t<name>Mario</name>
                \t<rating>Mature</rating>
                \t<releaseDate>2012-05-04</releaseDate>
                \t<reviewScore>85</reviewScore>
                </VideoGameRequest>""";

        given()
                .body(gameBodyXML)
                .contentType("application/xml")
                .accept("application/xml")
        .when()
                .post(VideoGameEndPoints.ALL_VIDEO_GAMES)
        .then();
    }

    //PUT Request
    @Test
    public void updateGame() {
        given()
                .body(gameBodyJSON)
        .when()
                .put("videogame/5")
        .then();
    }

    //DELETE Request
    @Test
    public void deleteGame() {
        given()
                .accept("text/plain")
        .when()
                .delete("videogame/5")
        .then();
    }

    //Path Parameters
    @Test
    public void getSingleGame() {
        given()
                .pathParams("videoGameId", 8)
        .when()
                .get(VideoGameEndPoints.SINGLE_VIDEO_GAME)
        .then();
    }

    //Object Serialization
    @Test
    public void testVideoGameSerializationByJSON() {
        VideoGame videoGame = new VideoGame("Shooter", "MyAwesomeGame", "Mature", "2018-04-04", 100);

        given()
                .body(videoGame)
        .when()
                .post(VideoGameEndPoints.ALL_VIDEO_GAMES)
        .then();

    }

    //Convert JSON Response to POJO
    @Test
    public void convertJsonToPojo() {
        Response response =
                given()
                        .pathParams("videoGameId", 5)
                                .when()
                        .get(VideoGameEndPoints.SINGLE_VIDEO_GAME);

        VideoGame videoGame = response.getBody().as(VideoGame.class);

        System.out.println(videoGame.toString());
    }

    //Validating Response against an XML Schema
    @Test
    public void testVideoGameSchemaXML() {
        given()
                .pathParams("videoGameId", 5)
                .accept("application/xml")
        .when()
                .get(VideoGameEndPoints.SINGLE_VIDEO_GAME)
        .then()
                .body(RestAssuredMatchers.matchesXsdInClasspath("VideoGameXSD.xsd"));
    }

    //Validating Response against a JSON Schema
    @Test
    public void testVideoGameSchemaJson() {
        given()
                .pathParams("videoGameId", 5)
        .when()
                .get(VideoGameEndPoints.SINGLE_VIDEO_GAME)
        .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("VideoGameJsonSchema.json"));
    }

    //Measuring Response Time in REST Assured
    @Test
    public void captureResponseTime() {
        long resposeTime = get(VideoGameEndPoints.ALL_VIDEO_GAMES).time();
        System.out.println("Response time in MS: " + resposeTime);
    }

    @Test
    public void assertOnResponseTime() {
        get(VideoGameEndPoints.ALL_VIDEO_GAMES)
                .then().time(lessThan(3000L));
    }
}