/**
 * Project: A00977249Assignment2
 * File: FileReader.java
 * Date: Jun 20, 2016
 * Time: 9:10:58 PM
 */
package a00977249.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.ApplicationException;
import a00977249.Gis;
import a00977249.data.Game;
import a00977249.data.GamertagResults;
import a00977249.data.Persona;
import a00977249.data.Player;
import a00977249.data.Score;
import a00977249.data.jointtable.PersonaScore;
import a00977249.data.jointtable.PlayerScore;

/**
 * @author Siamak Pourian, A009772249
 *
 *         DataReader Class
 */
public class DataReader {

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	public static final String ELEMENT_DELIMITER = "\\|";
	public static final String SCORES_DATA_WIN_FORMAT = "WIN";
	public static final String SCORES_DATA_LOSS_FORMAT = "LOSE";

	/**
	 * Zero-Parameter Constructor
	 */
	public DataReader() {
		LOG.debug("DataReader()");
	}

	/**
	 * Reads the Game data collection
	 *
	 * @param file
	 *            the file to be read
	 * @return a Map of Games
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	public static Map<String, Game> readGame(File file) throws ApplicationException, FileNotFoundException {
		Map<String, Game> gameMap = new HashMap<String, Game>();
		List<String> rows;

		LOG.debug("GameReader");
		rows = FileReader.read(file, Game.GAME_DATA_FORMAT);
		for (String row : rows) {
			String[] elements = row.split(ELEMENT_DELIMITER);
			if (elements.length != Game.NUMBER_OF_GAME_DATA_ELEMENTS) {
				throw new ApplicationException(
						String.format("Expected %d elements but got %d: %s", Persona.NUMBER_OF_PERSONA_DATA_ELEMENTS, elements.length, Arrays.toString(elements)));
			}
			Game game = new Game();
			int j = 0;
			game.setId(elements[j++]);
			game.setName(elements[j++]);
			game.setProducer(elements[j++]);
			gameMap.put(game.getId(), game);
		}
		LOG.debug(String.format("%s file content interpreted", file.getName()));
		return gameMap;
	}

	/**
	 * Reads the Personas data collection
	 * 
	 * @param file
	 *            the file to be read
	 * @return a Map of Personas
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	public static Map<Integer, Persona> readPersona(File file) throws ApplicationException, FileNotFoundException {
		Map<Integer, Persona> personaMap = new HashMap<Integer, Persona>();
		List<String> rows;

		LOG.debug("PersonaReader");
		rows = FileReader.read(file, Persona.PERSONA_DATA_FORMAT);
		for (String row : rows) {
			String[] elements = row.split(ELEMENT_DELIMITER);
			if (elements.length != Persona.NUMBER_OF_PERSONA_DATA_ELEMENTS) {
				throw new ApplicationException(
						String.format("Expected %d elements but got %d: %s", Persona.NUMBER_OF_PERSONA_DATA_ELEMENTS, elements.length, Arrays.toString(elements)));
			}
			Persona persona = new Persona();
			int j = 0;
			persona.setId(Integer.parseInt(elements[j++]));
			persona.setPlayerId(Integer.parseInt(elements[j++]));
			persona.setGamerTag(elements[j++]);
			persona.setPlatForm(elements[j++]);
			personaMap.put(persona.getId(), persona);
		}
		LOG.debug(String.format("%s file content interpreted", file.getName()));
		return personaMap;
	}

	/**
	 * Reads the player data collection
	 * 
	 * @param file
	 *            the file to be read
	 * @return a Map of Players
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	public static Map<Integer, Player> readPlayer(File file) throws ApplicationException, FileNotFoundException {
		Map<Integer, Player> playerMap = new HashMap<Integer, Player>();
		List<String> rows;

		LOG.debug("PlayerReader");
		rows = FileReader.read(file, Player.PLAYER_DATA_FORMAT);
		for (String row : rows) {
			String[] elements = row.split(ELEMENT_DELIMITER);
			if (elements.length != Player.NUMBER_OF_PLAYER_DATA_ELEMENTS) {
				throw new ApplicationException(
						String.format("Expected %d elements but got %d: %s", Player.NUMBER_OF_PLAYER_DATA_ELEMENTS, elements.length, Arrays.toString(elements)));
			}
			Player player = new Player();
			int j = 0;
			player.setId(Integer.parseInt(elements[j++]));
			player.setFirstName(elements[j++]);
			player.setLastName(elements[j++]);
			try {
				player.setEmailAddress(elements[j++]);
				;
				String yyyymmdd = elements[j];
				player.setBirthDate(yyyymmdd.substring(0, 4) + "-" + yyyymmdd.substring(4, 6) + "-" + yyyymmdd.substring(6, 8));

			} catch (ApplicationException | NumberFormatException e) {
				LOG.error(e.getMessage());
			}
			playerMap.put(player.getId(), player);
		}
		LOG.debug(String.format("%s file content interpreted", file.getName()));
		return playerMap;
	}

	/**
	 * Reads the Score data file
	 *
	 * @param file
	 *            the file to be read
	 * @return a Map of Scores
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	public static List<Score> readScore(File file) throws ApplicationException, FileNotFoundException {
		List<Score> scoreList = new ArrayList<Score>();
		List<String> rows;
		String win;

		LOG.debug("ScoreReader");
		rows = FileReader.read(file, Score.SCORE_DATA_FORMAT);
		for (String row : rows) {
			String[] elements = row.split(ELEMENT_DELIMITER);
			if (elements.length != Score.NUMBER_OF_SCORE_DATA_ELEMENTS) {
				throw new ApplicationException(
						String.format("Expected %d elements but got %d: %s", Persona.NUMBER_OF_PERSONA_DATA_ELEMENTS, elements.length, Arrays.toString(elements)));
			}
			Score score = new Score();
			int j = 0;
			score.setScoreId(scoreList.size() + 1);
			score.setPersonaId(Integer.parseInt(elements[j++]));
			score.setGameId(elements[j++]);
			win = elements[j].toUpperCase();
			if (win.equals(SCORES_DATA_WIN_FORMAT)) {
				score.setWin(true);
			} else if (win.equals(SCORES_DATA_LOSS_FORMAT)) {
				score.setWin(false);
			} else {
				throw new ApplicationException("The value of the 'WIN' element is unrecognized.");
			}
			scoreList.add(score);
		}
		LOG.debug(String.format("%s file content interpreted", file.getName()));
		return scoreList;
	}

	/**
	 * Generates the Map for Player Report
	 * 
	 * @param scoreList
	 *            Collection of scores
	 * @param personaMap
	 *            Map of personas
	 * 
	 * @return generated Map for outputting the report to a text file
	 */
	public static Map<Integer, PlayerScore> readPlayerReport(List<Score> scoreList, Map<Integer, Persona> personaMap) {

		LOG.debug("PlayerReportReader");
		Map<Integer, PlayerScore> playersScores = new HashMap<Integer, PlayerScore>();
		int personaId;
		int playerId;
		PlayerScore playerScore;
		Persona persona;
		for (Score score : scoreList) {
			personaId = score.getPersonaId();
			persona = personaMap.get(personaId);
			playerId = persona.getPlayerId();
			playerScore = playersScores.get(playerId);
			if (playerScore == null) {
				playerScore = new PlayerScore(playerId);
			}
			playerScore.incTotalGames();
			if (score.getWin()) {
				playerScore.incWins();
			} else {
				playerScore.incLoss();
			}
			playersScores.put(playerId, playerScore);
		}
		return playersScores;
	}

	/**
	 * Generates a Map for Leader board Report
	 * 
	 * @param scoreList
	 *            Collection of scores data
	 * @param personaMap
	 *            Map of personas data
	 * @param gameMap
	 *            Map of games data
	 * 
	 * @return a List for outputting the report to a text file
	 */
	public static List<PersonaScore> readLeaderBoardReport(List<Score> scoreList, Map<Integer, Persona> personaMap, Map<String, Game> gameMap) {
		int personaId;
		String gameName;
		String gameId;
		Persona persona;
		PersonaScore personaScore;
		Map<String, PersonaScore> personaScoreMap = new HashMap<String, PersonaScore>();

		LOG.debug("LeaderBoardReportReader");
		for (Score score : scoreList) {
			personaId = score.getPersonaId();
			gameId = score.getGameId();
			gameName = gameMap.get(gameId).getName();
			personaScore = personaScoreMap.get(personaId + gameId);
			if (personaScore == null) {
				personaScore = new PersonaScore(personaId, gameName);
			}
			if (score.getWin()) {
				personaScore.incWins();
			} else {
				personaScore.incLoss();
			}
			persona = personaMap.get(personaId);
			personaScore.setGamerTag(persona.getGamerTag());
			personaScore.setPlatForm(persona.getPlatForm());
			personaScore.setPersonaIdGameId(personaId + gameId);
			personaScoreMap.put(personaId + gameId, personaScore);
		}
		List<PersonaScore> personasScoreList = new ArrayList<>(personaScoreMap.values());
		return personasScoreList;
	}

	/**
	 * Generates the Map for Leader Board Report
	 *
	 * @param leaderBoardList
	 *            collection of the report elements
	 * @return Map<String, Integer> The map containing the total results for the gamertags
	 */
	public static Map<String, GamertagResults> getTotalGamertagPlayedCounts(List<PersonaScore> leaderBoardList) {

		Map<String, GamertagResults> playedCounts = new HashMap<String, GamertagResults>();

		String gameName;
		int totalWin;
		int totalLoss;
		GamertagResults result;
		for (PersonaScore personaScore : leaderBoardList) {
			gameName = personaScore.getGameName();
			totalWin = personaScore.getTotalWin();
			totalLoss = personaScore.getTotalLoss();
			result = playedCounts.get(gameName);
			if (result == null) {
				result = new GamertagResults(gameName, totalWin + totalLoss);
			} else {
				result.setTotalPlayed(result.getTotalPlayed() + totalWin + totalLoss);
			}
			playedCounts.put(gameName, result);
		}
		return playedCounts;
	}
}