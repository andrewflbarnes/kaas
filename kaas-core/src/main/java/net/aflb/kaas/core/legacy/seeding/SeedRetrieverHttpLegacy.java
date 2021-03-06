package net.aflb.kaas.core.legacy.seeding;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Team;

import java.util.Collections;
import java.util.List;

/**
 * This class retrieves information from results tables on the Kings Ski Club
 * website returning the result as a List of {@link Team} objects
 *
 * @author Barnesly
 */
public class SeedRetrieverHttpLegacy implements SeedRetriever {

    private static final int READ_TIMEOUT = 10000;
    // TODO Add/retrieve the URL string to/from PreferenceManager
    private static final String URL_STRING = "http://www.kingsski.org/component/leagues?tmpl=component&amp;id=";

    private int leagueId;

    public SeedRetrieverHttpLegacy(int leagueId) {
        this.leagueId = leagueId;
    }

    /**
     * Returns the results from a page at kingsski.org as {@link Team} objects.
     * This routine attempts to work out the club name (since this is not tied
     * to the results page), and any team it can't be determined for is left
     * blank and, mor importantly, the teamId is left as 0. Checks must be made
     * before propagating the team to the database to ensure that either an
     * existing or new club name is assigned. In the case where a new name is
     * assigned the {@link Club} will also require adding to the database
     *
     * @return a List of {@link Team} objects which were retrieved from the URL
     */
    @Override
    public List<Team> getSeeds() {
//        Document doc = retrieveSeedingPage(leagueId);
//        if (doc == null) {
//            return new ArrayList<>();
//        }

//        List<Team> teamList = extractTeams(null);
//
//        // We know what league and division we have, reset all scores for
//        // existing teams to 0. Doing it this way means we don't retain bad data
//        // i.e. teams which did have scores but are no longer seeded, and also
//        // means we don't have to delete any teams
//        if (teamList.size() > 0) {
//            Collections.sort(teamList);
//        }
//
//        return teamList;
        return Collections.emptyList();
    }

    /**
     *
     * @param leagueId
     * @return
     */
//    private static Document retrieveSeedingPage(int leagueId) {
//
//        StringBuilder content = new StringBuilder(16000);
//        URLConnection connection;
//
//        // Initialise the URL we will connect to
//        String urlString = URL_STRING + Integer.toString(leagueId);
//
//        // Attempt to retrieve the page from the URL
//        try {
//            // Open a connection to the URL and set the read timeout
//            connection = new URL(urlString).openConnection();
//            connection.setReadTimeout(READ_TIMEOUT);
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line);
//            }
//        } catch (MalformedURLException e) {
//            log.warn("Invalid URL: {}", urlString);
//        } catch (IOException e) {
//            log.warn("Unable to connect to URL: {}", urlString);
//        }
//
//        // If we couldn't retrieve the webpage then return
//        if (content == null || content.length() == 0) {
//            log.warn("Unable to retrieve information from URL: {}", urlString);
//            return null;
//        }
//
//        // Parse the retrieved HTML
//        return Jsoup.parse(content.toString());
//    }

    /**
     *
     * @param seedingDocument
     * @return
     */
//    private static SeedingDetails extractSeedingDetails(Document seedingDocument) {
//        SeedingDetails details = new SeedingDetails();
//
//        Elements h1s = seedingDocument.select("h1");
//        Element h1;
//        for (int i = 0, n = h1s.size(); i < n; i++) {
//            h1 = h1s.get(i);
//            if (h1.text().toUpperCase().contains(Division.MIXED.toUpperCase())) {
//                details.division = Division.MIXED;
//            } else if (h1.text().toUpperCase().contains(Division.LADIES.toUpperCase())) {
//                details.division = Division.LADIES;
//            } else if (h1.text().toUpperCase().contains(Division.BOARD.toUpperCase())) {
//                details.division = Division.BOARD;
//            }
//
//            if (h1.text().toUpperCase().contains(League.NORTHERN.toUpperCase())) {
//                details.league = League.NORTHERN;
//            } else if (h1.text().toUpperCase().contains(League.SOUTHERN.toUpperCase())) {
//                details.league = League.SOUTHERN;
//            } else if (h1.text().toUpperCase().contains(League.MIDLANDS.toUpperCase())) {
//                details.league = League.MIDLANDS;
//            } else if (h1.text().toUpperCase().contains(League.WESTERN.toUpperCase())) {
//                details.league = League.WESTERN;
//            }
//        }
//
//        //TODO Raise exception here
//        if (details.division.isEmpty()) {
//            log.error("division not detected on seed retrieval");
//        }
//        if (details.league.isEmpty()) {
//            log.error("league not detected on seed retrieval");
//        }
//        log.error("results retrieved for {} : {}", details.league, details.division);
//
//        return details;
//    }
//
//    /**
//     *
//     *
//     * @param seedingDocument
//     * @return
//     */
//    private static List<Team> extractTeams(Document seedingDocument) {
//        // Get the list of all clubs for this
//        List<Team> teamList = new ArrayList<>(32);
//        SeedingDetails seedingDetails = extractSeedingDetails(seedingDocument);
//
//        // Retrieve the table
//        Elements tables = seedingDocument.select("table");
//        Elements trs;
//        Elements tds;
//        for (int i = 0, n = tables.size(); i < n; i++) {
//            //for (Element table : tables) {
//            // Retrieve the table rows
//            //trs = table.select("tr");
//            trs = tables.get(i).select("tr");
//            // Retrieve the data in each cell
//            for (int j = 0, m = trs.size(); j < m; j++) {
//                tds = trs.get(j).select("td");
//
//                Team team = new Team();
//                team.setDivision(seedingDetails.division);
//                team.setLeague(seedingDetails.league);
//                // Ignore the first and last element - the first is an ordinal
//                // position and the last is the total (we work this out
//                // ourselves)
//                for (int k = 1, l = tds.size(); k < l - 1; k++) {
//                    switch (k) {
//                        case 1:
//                            team.setTeamName(tds.get(k).text());
//                            break;
//                        case 2:
//                            team.setScoreR1(Integer.parseInt(tds.get(k).text()));
//                            break;
//                        case 3:
//                            team.setScoreR2(Integer.parseInt(tds.get(k).text()));
//                            break;
//                        case 4:
//                            team.setScoreR3(Integer.parseInt(tds.get(k).text()));
//                            break;
//                        case 5:
//                            team.setScoreR4(Integer.parseInt(tds.get(k).text()));
//                            break;
//                        case 6:
//                            team.setScoreR5(Integer.parseInt(tds.get(k).text()));
//                            break;
//                        default:
//                    }
//                }
//
//                log.debug("{} retrieved", team.toString());
//
//                // Ignore empty entries
//                if (team.getTeamName() != null && !team.getTeamName().isEmpty()) {
//                    teamList.add(team);
//                }
//            }
//            // trtd now contains the desired array for this table
//        }
//
//        return teamList;
//    }

    /**
     * Class for storing details of the current seeds being retrieved
     */
    private static class SeedingDetails {
        public String league;
        public String division;
    }
}
