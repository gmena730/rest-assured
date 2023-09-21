import config.VideoGameConfig;
import config.VideoGameEndPoints;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class MyFirstTest extends VideoGameConfig  {

    @Test
    public void myFirstTest() {
        given()
                .log().all()
        .when()
                .get("/videogame")
        .then()
                .log().all();

    }

    public void myFirstTestWithEndPoint() {
            get(VideoGameEndPoints.ALL_VIDEO_GAMES)
                    .then().log().all();
    }
}
