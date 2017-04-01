/**
 * Project: A00977249Assignment2
 * File: ScoreDao.java
 * Date: Jun 23, 2016
 * Time: 2:58:46 PM
 */
package a00977249.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.data.Score;
import a00977249.database.Database;

/**
 * @author Siamak Pourian, A009772249
 *
 *         ScoreDao Class
 */
public class ScoreDao extends Dao {
	public static final String TABLE_NAME = "Score";

	private static final Logger LOG = LogManager.getLogger(ScoreDao.class);

	/**
	 * Overloaded Constructor
	 */
	public ScoreDao(Database database) {
		super(database, TABLE_NAME);
	}

	/**
	 * Creates the Player table and the initializes the SQL statement
	 */
	@Override
	public void create() throws SQLException {
		String createStatement = String.format("create table %s(%s INTEGER, %s INTEGER, %s VARCHAR(4), %s VARCHAR(4), primary key (%s) )", tableName, //
				Fields.SCOREID, Fields.PERSONAID, Fields.GAMEID, Fields.WIN, Fields.SCOREID);
		super.create(createStatement);
	}

	/**
	 * Adds a Score to the table
	 * 
	 * @param score
	 *            to be added to the table
	 * @throws SQLException
	 */
	public void add(Score score) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values(%d, %d, '%s', '%s')", tableName, //
					score.getScoreId(), score.getPersonaId(), score.getGameId(), score.getWinToString());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieves a Score by scoreId
	 * 
	 * @param scoreId
	 *            the search key
	 * @return the score matching the key
	 * @throws SQLException
	 * @throws Exception
	 */
	public Score get(int scoreId) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Score score = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s =%d", tableName, Fields.SCOREID, scoreId);
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
				score = new Score();
				score.setScoreId(resultSet.getInt(Fields.SCOREID.name()));
				score.setPersonaId(resultSet.getInt(Fields.PERSONAID.name()));
				score.setGameId(resultSet.getString(Fields.GAMEID.name()));
				score.setWinToBoolean(resultSet.getString(Fields.WIN.name()));
			}
		} finally {
			close(statement);
		}
		return score;
	}

	/**
	 * Updates a Score
	 * 
	 * @param score
	 *            to be updated
	 */
	public void update(Score score) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s=%d, %s=%d, %s='%s', %s='%s' WHERE %s=%d", tableName, //
					Fields.SCOREID, score.getScoreId(), //
					Fields.PERSONAID, score.getPersonaId(), //
					Fields.GAMEID, score.getGameId(), //
					Fields.WIN, score.getWinToString(), //
					Fields.SCOREID, score.getPersonaId()); //
			System.out.println(sqlString);
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Deletes the Score from the table
	 * 
	 * @param score
	 *            to be deleted
	 */
	public void delete(Score score) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s=%d", tableName, Fields.SCOREID, score.getScoreId());
			LOG.debug(sqlString);
			System.out.println(sqlString);
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

		SCOREID(1), PERSONAID(2), GAMEID(2), WIN(4);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}
}
