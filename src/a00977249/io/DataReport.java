/**
 * Project: A00977249Assignment2
 * File: DataReport.java
 * Date: Jun 28, 2016
 * Time: 12:59:11 PM
 */
package a00977249.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import a00977249.data.Player;
import a00977249.data.jointtable.PersonaScore;
import a00977249.data.jointtable.PlayerScore;

/**
 * @author Siamak Pourian, A009772249
 *
 *         DataReport Class
 */
public class DataReport {

	public static final String PLAYER_ROW_FORMAT = "%-11d %-20s %-28s %-17s %10d %20d";
	public static final String PERSONA_ROW_FORMAT = "%-13s %-18s %17s %15s";

	/**
	 * private Zero-Parameter Constructor to prevent initialization
	 */
	private DataReport() {
	}

	/**
	 * Outputs the report to a text file
	 * 
	 * @param playerList
	 *            collection of players
	 * @param playersScore
	 *            Map of PersonaScores
	 * @throws FileNotFoundException
	 */
	public static String[] toArray(List<Player> playerList, Map<Integer, PlayerScore> playersScore) throws IOException {

		Player player;
		PlayerScore playerScore;
		int capacity = playerList.size();
		String[] playerReport = new String[capacity];

		for (int i = 0; i < capacity; i++) {
			player = playerList.get(i);
			playerScore = playersScore.get(player.getId());
			playerReport[i] = String.format(PLAYER_ROW_FORMAT, player.getId(), player.getFirstName().concat(" " + player.getLastName()), player.getEmailAddress(),
					player.getBirthDate().toString(), playerScore.getTotalGamesPlayed(), playerScore.getWins());
		}
		return playerReport;
	}

	public static String[] toArray(List<PersonaScore> list) {
		int capacity = list.size();
		String[] reportList = new String[capacity];
		PersonaScore personaScore;
		for (int i = 0; i < capacity; i++) {
			personaScore = list.get(i);
			reportList[i] = String.format(PERSONA_ROW_FORMAT, personaScore.getTotalWin() + ":" + personaScore.getTotalLoss(), personaScore.getGameName(),
					personaScore.getGamerTag(), personaScore.getPlatForm());
		}
		return reportList;
	}
}