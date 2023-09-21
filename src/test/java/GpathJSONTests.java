import config.FootballConfig;
import config.VideoGameConfig;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class GpathJSONTests extends FootballConfig {

    //GPath JSON Part 1 - Setup and basic find
    @Test
    public void extractMapOfElementsWithFind() {
        Response response = get("competitions/2021/teams");

        Map<String, ?> allTeamsDataForSingleTeam = response.path("teams.find { it.name == 'Manchester United FC' }");

        System.out.println("Map of team data = " + allTeamsDataForSingleTeam);
    }

    @Test
    public void extractSingleValueWithFind() {
        Response response = get("teams/57");

        String certainPlayer = response.path("squad.find { it.id == 3827 }.name");

        System.out.println("Name of Player: " + certainPlayer);
    }

    //GPath JSON Part 2 - Using findAll to extract multiple data
    @Test
    public void extractListOfValuesWithFindAll() {
        Response response = get("teams/57");

        List<String> playerName = response.path("squad.findAll { it.id >= 7784 }.name");

        System.out.println("List of Players: " + playerName);

    }

    //GPath JSON Part 3 - Using Min, Max, Collect and Sum
    @Test
    public void extractSingleValueWithHighestNumber() {
        Response response = get("teams/57");

        String playerName = response.path("squad.max { it.id }.name");

        System.out.println("Player with the highest id: " + playerName);

    }

    @Test
    public void extractMultipleValuesAndThenSumThem() {
        Response response = get("teams/57");

        int sumOfIds = response.path("squad.collect { it.id }.sum()");

        System.out.println("Sum of all IDs:" + sumOfIds);

    }

    //GPath JSON Part 4 - Combining finds and using parameters
    @Test
    public void extractMapWithFindAllAndFindWithParameter() {

        String position = "Offence";
        String nationality = "England";

        Response response = get("teams/57");

        Map<String, ?> playerOfCertainPosition = response.path(
                    "squad.findAll { it.position == '%s' }.find { it.nationality == '%s' }",
                position, nationality

        );

        System.out.println("Details of players: " + playerOfCertainPosition);

    }

    @Test
    public void extractMultiplePlayers() {

        String position = "Offence";
        String nationality = "England";

        Response response = get("teams/57");

        ArrayList<Map<String, ?>> allPlayerOfCertainPosition = response.path(
                "squad.findAll { it.position == '%s' }.findAll { it.nationality == '%s' }",
                position, nationality

        );

        System.out.println("Details of players: " + allPlayerOfCertainPosition);

    }
}
