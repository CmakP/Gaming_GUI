/**
 * Project: A00977249Assignment2
 * File: TotalGameertagCountsDao.java
 * Date: Jun 28, 2016
 * Time: 1:08:56 PM
 */
package a00977249.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.data.GamertagResults;
import a00977249.database.Database;

/**
 * @author Siamak Pourian, A009772249
 *
 *         TotalGamertagCountsDao Class
 */
public class GameCountsDao extends Dao {

	public static final String TABLE_NAME = "GameCountsDao";

	private static final Logger LOG = LogManager.getLogger(GameCountsDao.class);

	/**
	 * Overloaded Constructor
	 */
	public GameCountsDao(Database database) {
		super(database, TABLE_NAME);
	}

	/**
	 * Creates the Game table and the initializes the SQL statement
	 */
	@Override
	public void create() throws SQLException {
		String createStatement = String.format("create table %s(%s VARCHAR(15),%s Integer, primary key (%s) )", tableName, //
				Fields.GAMERTAG, Fields.TOTALCOUNTS, Fields.GAMERTAG);
		super.create(createStatement);
	}

	/**
	 * Adds a gamertag result to the table
	 * 
	 * @param gamertagResult
	 *            the result to be added to the table
	 * @throws SQLException
	 */
	public void add(GamertagResults gamertagResult) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values('%s', %d)", tableName, //
					gamertagResult.getGameName(), gamertagResult.getTotalPlayed());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieves a List of gamerTag results
	 * 
	 * @return a List of gamertag results
	 * @throws SQLException
	 */
	public String[] getResults() throws SQLException {
		String[] results = new String[TableSize.getTableSize(database, TABLE_NAME)];
		Connection connection;
		Statement statement = null;

		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s", tableName);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Student
			// throw an exception if we get more than one result
			int index = 0;
			while (resultSet.next()) {
				results[index] = resultSet.getString(Fields.GAMERTAG.name()) + "  " + resultSet.getInt(Fields.TOTALCOUNTS.name());
				index++;
			}
			LOG.info(String.format("Loaded %d gamertag results from the database", index));
		} finally {
			close(statement);
		}
		return results;
	}

	/**
	 * Fields Enum
	 */
	public enum Fields {

		GAMERTAG(1), TOTALCOUNTS(2);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}

}
