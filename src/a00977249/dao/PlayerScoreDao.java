/**
 * Project: A00977249Assignment2
 * File: PlayerScore.java
 * Date: Jun 23, 2016
 * Time: 5:23:27 PM
 */
package a00977249.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.data.jointtable.PlayerScore;
import a00977249.database.Database;

/**
 * @author Siamak Pourian, A009772249
 *
 *         PlayerScoreDao Class
 */
public class PlayerScoreDao extends Dao {

	public static final String TABLE_NAME = "PlayerScore";

	private static final Logger LOG = LogManager.getLogger(PlayerScoreDao.class);

	/**
	 * Overloaded Constructor
	 */
	public PlayerScoreDao(Database database) {
		super(database, TABLE_NAME);
	}

	/**
	 * Creates the Player table and the initializes the SQL statement
	 */
	@Override
	public void create() throws SQLException {
		String createStatement = String.format("create table %s(%s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, primary key (%s) )", tableName, //
				Fields.PLAYERID, Fields.WINS, Fields.LOSS, Fields.TOTALGAMESPLAYED, Fields.PLAYERID);
		super.create(createStatement);
	}

	/**
	 * Adds a PlayerScore to the table
	 * 
	 * @param playerScore
	 *            to be added to the table
	 * @throws SQLException
	 */
	public void add(PlayerScore playerScore) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values(%d, %d, %d, %d)", tableName, //
					playerScore.getPlayerId(), playerScore.getWins(), playerScore.getLoss(), playerScore.getTotalGamesPlayed());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieves a PlayerScore by playerId
	 * 
	 * @param playerId
	 *            the search key
	 * @return the PlayerScore matching the key
	 * @throws SQLException
	 * @throws Exception
	 */
	public PlayerScore get(int playerId) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		PlayerScore playerScore = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", tableName, Fields.PLAYERID, playerId);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Student
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}
				playerScore = new PlayerScore(playerId);
				playerScore.setId(resultSet.getInt(Fields.PLAYERID.name()));
				playerScore.setWins(resultSet.getInt(Fields.WINS.name()));
				playerScore.setLoss(resultSet.getInt(Fields.LOSS.name()));
				playerScore.setTotalGamesPlayed(resultSet.getInt(Fields.TOTALGAMESPLAYED.name()));
			}
		} finally {
			close(statement);
		}
		return playerScore;
	}

	/**
	 * Retrieves a PlayerScore List
	 * 
	 * @return a PlayerScore collection created by Dao
	 * @throws SQLException
	 * @throws Exception
	 */
	public Map<Integer, PlayerScore> getPlayerReportDaoMap() throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		PlayerScore playerScore = null;
		Map<Integer, PlayerScore> playerReportDaoMap = new HashMap<Integer, PlayerScore>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s", tableName);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);

			while (resultSet.next()) {
				playerScore = new PlayerScore();
				int playerId = resultSet.getInt(Fields.PLAYERID.name());
				playerScore.setId(playerId);
				playerScore.setWins(resultSet.getInt(Fields.WINS.name()));
				playerScore.setLoss(resultSet.getInt(Fields.LOSS.name()));
				playerScore.setTotalGamesPlayed(resultSet.getInt(Fields.TOTALGAMESPLAYED.name()));
				playerReportDaoMap.put(playerId, playerScore);
			}
		} finally {
			close(statement);
		}
		return playerReportDaoMap;
	}

	/**
	 * Updates a PlayerScore
	 * 
	 * @param playerScore
	 *            to be updated
	 */
	public void update(PlayerScore playerScore) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s=%d, %s=%d, %s=%d, %s=%d WHERE %s=%d", tableName, //
					Fields.PLAYERID, playerScore.getPlayerId(), //
					Fields.WINS, playerScore.getWins(), //
					Fields.LOSS, playerScore.getLoss(), //
					Fields.TOTALGAMESPLAYED, playerScore.getTotalGamesPlayed(), //
					Fields.PLAYERID, playerScore.getPlayerId()); //
			System.out.println(sqlString);
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Deletes the PlayerScore from the table
	 * 
	 * @param playerScore
	 *            to be deleted
	 */
	public void delete(PlayerScore playerScore) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s=%d", tableName, Fields.PLAYERID, playerScore.getPlayerId());
			System.out.println(sqlString);
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Fields Enum
	 */
	public enum Fields {

		PLAYERID(1), WINS(2), LOSS(3), TOTALGAMESPLAYED(4);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}

}
