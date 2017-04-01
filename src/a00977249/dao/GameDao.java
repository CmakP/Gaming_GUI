/**
 * Project: A00977249Assignment2
 * File: GameDao.java
 * Date: Jun 23, 2016
 * Time: 1:52:59 PM
 */
package a00977249.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.data.Game;
import a00977249.database.Database;

/**
 * @author Siamak Pourian, A009772249
 *
 *         GameDao Class
 */
public class GameDao extends Dao {
	public static final String TABLE_NAME = "Game";

	private static final Logger LOG = LogManager.getLogger(GameDao.class);

	/**
	 * Overloaded Constructor
	 */
	public GameDao(Database database) {
		super(database, TABLE_NAME);
	}

	/**
	 * Creates the Game table and the initializes the SQL statement
	 */
	@Override
	public void create() throws SQLException {
		String createStatement = String.format("create table %s(%s VARCHAR(5), %s VARCHAR(30), %s VARCHAR(15), primary key (%s) )", tableName, //
				Fields.ID, Fields.NAME, Fields.PRODUCER, Fields.ID);
		super.create(createStatement);
	}

	/**
	 * Adds a Game to the table
	 * 
	 * @param game
	 *            to be added to the table
	 * @throws SQLException
	 */
	public void add(Game game) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values('%s', '%s', '%s')", tableName, //
					game.getId(), game.getName(), game.getProducer());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieves a game using id
	 * 
	 * @param id
	 *            the search key
	 * @return the game matching the key
	 * @throws SQLException
	 * @throws Exception
	 */
	public Game get(String id) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Game game = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.ID, id);
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
				game = new Game();
				game.setId(resultSet.getString(Fields.ID.name()));
				game.setName(resultSet.getString(Fields.NAME.name()));
				game.setProducer(resultSet.getString(Fields.PRODUCER.name()));
			}
		} finally {
			close(statement);
		}
		return game;
	}

	/**
	 * Updates a Game
	 * 
	 * @param game
	 *            to be updated
	 */
	public void update(Game game) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s'' WHERE %s='%s'", tableName, //
					Fields.ID, game.getId(), //
					Fields.NAME, game.getName(), //
					Fields.PRODUCER, game.getProducer(), //
					Fields.ID, game.getId()); //
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Deletes the game from the table
	 * 
	 * @param game
	 *            to be deleted
	 */
	public void delete(Game game) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.ID, game.getId());
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

		ID(1), NAME(2), PRODUCER(3);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}
}
