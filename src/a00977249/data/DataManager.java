/**
 * Project: A00977249Assignment2
 * File: DataManager.java
 * Date: Jun 23, 2016
 * Time: 8:54:04 PM
 */
package a00977249.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.ApplicationException;
import a00977249.dao.GameDao;
import a00977249.dao.PersonaDao;
import a00977249.dao.PersonaScoreDao;
import a00977249.dao.PlayerDao;
import a00977249.dao.PlayerScoreDao;
import a00977249.dao.ScoreDao;
import a00977249.dao.GameCountsDao;
import a00977249.data.jointtable.PersonaScore;
import a00977249.data.jointtable.PlayerScore;
import a00977249.database.Database;
import a00977249.io.DataReader;

/**
 * @author Siamak Pourian, A009772249
 *
 *         DataManager Class
 */
public class DataManager {

	private static final String[] FILE_NAMES = { "games.dat", "personas.dat", "players.dat", "scores.dat" };
	private static final Logger LOG = LogManager.getLogger(DataManager.class);

	private Database database;
	private File[] dataFiles;

	private GameDao gameDao;
	private PersonaDao personaDao;
	private PlayerDao playerDao;
	private ScoreDao scoreDao;
	private PlayerScoreDao playerScoreDao;
	private PersonaScoreDao personaScoreDao;
	private GameCountsDao gameCountsDao;

	private Map<String, GamertagResults> totalGameCounts;
	private Map<String, Game> gameMap;
	private Map<Integer, Persona> personaMap;
	private Map<Integer, Player> playerMap;
	private List<Score> scoreList;

	private List<PersonaScore> leaderBoardList;
	private Map<Integer, PlayerScore> playersReport;

	/**
	 * 
	 */
	public DataManager(Database database) {
		this.database = database;
	}

	/**
	 * Accesses all data files, parses through the contents and stores them in collections,
	 * then creates the tables only if they don't exist and finally loads the collections
	 * into the corresponding databases
	 * 
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	public void loadData() throws FileNotFoundException, ApplicationException, SQLException {

		gameDao = new GameDao(database);
		personaDao = new PersonaDao(database);
		playerDao = new PlayerDao(database);
		scoreDao = new ScoreDao(database);
		playerScoreDao = new PlayerScoreDao(database);
		personaScoreDao = new PersonaScoreDao(database);
		gameCountsDao = new GameCountsDao(database);

		try {
			// access the data files
			accessFiles();

			// load data from files to collections
			loadAllData();

			// checks if tables exist, else creates them
			createTables();

			// load the files into tables
			loadTables();

		} catch (SQLException e) {
			LOG.fatal(e.getMessage());
		}
	}

	/**
	 * Creates an empty collection of files
	 */
	private void accessFiles() {

		dataFiles = new File[FILE_NAMES.length];

		try {
			for (int i = 0; i < FILE_NAMES.length; i++) {
				dataFiles[i] = new File(FILE_NAMES[i]);
				if (!dataFiles[i].exists()) {
					throw new FileNotFoundException(String.format("\nFile %s doesn't exist in the source directory. Try adding it to the src path.", FILE_NAMES[i]));
				}
			}
		} catch (FileNotFoundException e) {
			LOG.fatal(e.getMessage());
		}
		LOG.debug("Files accessed");
	}

	/**
	 * Loads the Files into collections
	 * 
	 * @throws FileNotFoundException
	 */
	private void loadAllData() throws ApplicationException, FileNotFoundException {
		int i = 0;
		this.gameMap = DataReader.readGame(dataFiles[i++]);
		this.personaMap = DataReader.readPersona(dataFiles[i++]);
		this.playerMap = DataReader.readPlayer(dataFiles[i++]);
		this.scoreList = DataReader.readScore(dataFiles[i]);

		this.playersReport = DataReader.readPlayerReport(scoreList, personaMap);
		this.leaderBoardList = DataReader.readLeaderBoardReport(scoreList, personaMap, gameMap);
		this.totalGameCounts = DataReader.getTotalGamertagPlayedCounts(leaderBoardList);
	}

	/**
	 * Loads the collections into corresponding table
	 * 
	 * @throws SQLException
	 */
	private void loadTables() throws SQLException {
		populateGameTable();
		populatePersonaTable();
		populatePlayerTable();
		populateScoreTable();
		populatePlayerScoreTable();
		populatePersonaScoreTable();
		populateGameCountsTable();
	}

	/**
	 * Loads the Game Map into the Database
	 * 
	 * @throws SQLException
	 */
	private void populateGameTable() throws SQLException {
		try {
			for (Map.Entry<String, Game> entry : gameMap.entrySet()) {
				gameDao.add(entry.getValue());
			}
			LOG.debug("Loading 'games.dat' file in database completed successfully");
		} catch (SQLException e) {
			LOG.fatal(e);
		}
	}

	/**
	 * Loads the Player Map into the Database
	 * 
	 * @throws SQLException
	 */
	private void populatePlayerTable() throws SQLException {
		try {
			for (Map.Entry<Integer, Player> entry : playerMap.entrySet()) {
				playerDao.add(entry.getValue());
			}
			LOG.debug("Loading 'players.dat' file in database completed successfully");
		} catch (SQLException e) {
			LOG.fatal(e);
		}
	}

	/**
	 * Loads the Persona Map into the Database
	 * 
	 * @throws SQLException
	 */
	private void populatePersonaTable() throws SQLException {
		try {
			for (Map.Entry<Integer, Persona> entry : personaMap.entrySet()) {
				personaDao.add(entry.getValue());
			}
			LOG.debug("Loading 'personas.dat' file in database completed successfully");
		} catch (SQLException e) {
			LOG.fatal(e);
		}
	}

	/**
	 * Loads the Score list into the Database
	 * 
	 * @throws SQLException
	 */
	private void populateScoreTable() throws SQLException {
		try {
			for (Score score : scoreList) {
				scoreDao.add(score);
			}
			LOG.debug("Loading 'scores.dat' file in database completed successfully");
		} catch (SQLException e) {
			LOG.fatal(e);
		}
	}

	/**
	 * Loads the playerReport data collection into the Database
	 * 
	 * @throws SQLException
	 */
	private void populatePlayerScoreTable() throws SQLException {
		try {
			for (Map.Entry<Integer, PlayerScore> entry : playersReport.entrySet()) {
				playerScoreDao.add(entry.getValue());
			}
			LOG.debug("Loading PlayersScores collection in database completed successfully");
		} catch (SQLException e) {
			LOG.fatal(e);
		}
	}

	/**
	 * Loads the leaderBoardReport data collection into the Database
	 * 
	 * @throws SQLException
	 */
	private void populatePersonaScoreTable() throws SQLException {
		try {
			for (PersonaScore personaScore : leaderBoardList) {
				personaScoreDao.add(personaScore);
			}
			LOG.debug("Loading PlayersScores collection in database completed successfully");
		} catch (SQLException e) {
			LOG.fatal(e);
		}
	}

	private void populateGameCountsTable() {
		try {
			for (Map.Entry<String, GamertagResults> entry : totalGameCounts.entrySet()) {
				gameCountsDao.add(entry.getValue());
			}
			LOG.debug("Loading GamertagResults collection in database completed successfully");
		} catch (SQLException e) {
			LOG.fatal(e);
		}
	}

	/**
	 * Checks if the tables have already been created or not
	 * if not, creates it
	 * 
	 * @throws SQLException
	 */
	private void createTables() throws SQLException {
		if (!Database.tableExists(GameDao.TABLE_NAME)) {
			gameDao.create();
			LOG.debug(String.format("%s table created", GameDao.TABLE_NAME));
		} else {
			LOG.debug(String.format("%s table already exists in the database", GameDao.TABLE_NAME));
		}
		if (!Database.tableExists(PersonaDao.TABLE_NAME)) {
			personaDao.create();
			LOG.debug(String.format("%s table created", PersonaDao.TABLE_NAME));
		} else {
			LOG.debug(String.format("%s table already exists in the database", PersonaDao.TABLE_NAME));
		}
		if (!Database.tableExists(PlayerDao.TABLE_NAME)) {
			playerDao.create();
			LOG.debug(String.format("%s table created", PlayerDao.TABLE_NAME));
		} else {
			LOG.debug(String.format("%s table already exists in the database", PlayerDao.TABLE_NAME));
		}
		if (!Database.tableExists(ScoreDao.TABLE_NAME)) {
			scoreDao.create();
			if (Database.tableExists(PlayerScoreDao.TABLE_NAME)) {
				playerScoreDao.drop();
			}
			if (Database.tableExists(PersonaScoreDao.TABLE_NAME)) {
				personaScoreDao.drop();
			}
			if (Database.tableExists(GameCountsDao.TABLE_NAME)) {
				gameCountsDao.drop();
			}
			LOG.debug(String.format("%s table created", ScoreDao.TABLE_NAME));
		} else {
			LOG.debug(String.format("%s table already exists in the database", ScoreDao.TABLE_NAME));
		}
		if (!Database.tableExists(PlayerScoreDao.TABLE_NAME)) {
			playerScoreDao.create();
			LOG.debug(String.format("%s table created", PlayerScoreDao.TABLE_NAME));
		} else {
			LOG.debug(String.format("%s table already exists in the database", PlayerScoreDao.TABLE_NAME));
		}
		if (!Database.tableExists(PersonaScoreDao.TABLE_NAME)) {
			personaScoreDao.create();
			LOG.debug(String.format("%s table created", PersonaScoreDao.TABLE_NAME));
		} else {
			LOG.debug(String.format("%s table already exists in the database", PersonaScoreDao.TABLE_NAME));
		}
		if (!Database.tableExists(GameCountsDao.TABLE_NAME)) {
			gameCountsDao.create();
		} else {
			LOG.debug(String.format("%s table already exists in the database", GameCountsDao.TABLE_NAME));
		}
	}

	/**
	 * @return the gameDao
	 */
	public GameDao getGameDao() {
		return gameDao;
	}

	/**
	 * @return the personaDao
	 */
	public PersonaDao getPersonaDao() {
		return personaDao;
	}

	/**
	 * @return the playerDao
	 */
	public PlayerDao getPlayerDao() {
		return playerDao;
	}

	/**
	 * @return the scoreDao
	 */
	public ScoreDao getScoreDao() {
		return scoreDao;
	}

	/**
	 * @return the playerScoreDao
	 */
	public PlayerScoreDao getPlayerScoreDao() {
		return playerScoreDao;
	}

	/**
	 * @return the gameMap
	 */
	public Map<String, Game> getGameMap() {
		return gameMap;
	}

	/**
	 * @return the personaMap
	 */
	public Map<Integer, Persona> getPersonaMap() {
		return personaMap;
	}

	/**
	 * @return the playerMap
	 */
	public Map<Integer, Player> getPlayerMap() {
		return playerMap;
	}

	/**
	 * @return the scoreList
	 */
	public List<Score> getScoreList() {
		return scoreList;
	}

	/**
	 * @return the leaderBoardList
	 */
	public List<PersonaScore> getLeaderBoardList() {
		return leaderBoardList;
	}

	/**
	 * @return the playersReport
	 */
	public Map<Integer, PlayerScore> getPlayersReport() {
		return playersReport;
	}

	/**
	 * @return the personaScoreDao
	 */
	public PersonaScoreDao getPersonaScoreDao() {
		return personaScoreDao;
	}

	/**
	 * @return the gameCountsDao
	 */
	public GameCountsDao getGameCountsDao() {
		return gameCountsDao;
	}

	/**
	 * @return the totalGameCounts
	 */
	public Map<String, GamertagResults> getTotalGameCounts() {
		return totalGameCounts;
	}
}
