/**
 * Project: A00977249Assignment2
 * File: PlayerDao.java
 * Date: Jun 20, 2016
 * Time: 8:59:05 PM
 */
package a00977249.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.ApplicationException;
import a00977249.data.Player;
import a00977249.database.Database;

/**
 * @author Siamak Pourian, A009772249
 *
 *         PlayerDao Class
 */
public class PlayerDao extends Dao {

	public static final String TABLE_NAME = "Player";

	private static final Logger LOG = LogManager.getLogger(PlayerDao.class);

	/**
	 * Overloaded Constructor
	 */
	public PlayerDao(Database database) {
		super(database, TABLE_NAME);
	}

	/**
	 * Creates the Player table and the initializes the SQL statement
	 */
	@Override
	public void create() throws SQLException {
		String createStatement = String.format("create table %s(%s INTEGER, %s VARCHAR(10), %s VARCHAR(10), %s VARCHAR(30), %s VARCHAR(10), primary key (%s) )", tableName, //
				Fields.ID, Fields.FIRSTNAME, Fields.LASTNAME, Fields.EMAILADDRESS, Fields.BIRTHDATE, Fields.ID);
		super.create(createStatement);
	}

	/**
	 * Retrieves a List of firstName and lastName of players
	 * 
	 * @return an array of Player's firstName and lastName
	 * @throws SQLException
	 */
	public String[] getFirstLastName() throws SQLException {
		String[] firstLastName = new String[TableSize.getTableSize(database, TABLE_NAME)];
		Connection connection;
		Statement statement = null;

		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s", tableName);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);

			int index = 0;
			while (resultSet.next()) {
				firstLastName[index] = String.format("%s %s", resultSet.getString(Fields.FIRSTNAME.name()), resultSet.getString(Fields.LASTNAME.name()));
				index++;
			}
		} finally {
			close(statement);
		}
		return firstLastName;
	}

	/**
	 * Adds a Player to the table
	 * 
	 * @param player
	 *            to be added to the table
	 * @throws SQLException
	 */
	public void add(Player player) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values(%d, '%s', '%s', '%s', '%s')", tableName, //
					player.getId(), player.getFirstName(), player.getLastName(), player.getEmailAddress(), player.getBirthDate());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieves a single player using their id
	 * 
	 * @param id
	 *            the search key
	 * @return the Player matching the key
	 * @throws SQLException
	 * @throws Exception
	 */
	public Player get(int id) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Player player = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", tableName, Fields.ID, id);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);

			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}
				player = new Player();
				player.setId(resultSet.getInt(Fields.ID.name()));
				player.setFirstName(resultSet.getString(Fields.FIRSTNAME.name()));
				player.setLastName(resultSet.getString(Fields.LASTNAME.name()));
				player.setEmailAddress(resultSet.getString(Fields.EMAILADDRESS.name()));
				player.setBirthDate(resultSet.getString(Fields.BIRTHDATE.name()));
			}
		} finally {
			close(statement);
		}
		return player;
	}

	/**
	 * Retrieves a collection of Players
	 * 
	 * @return the Player collection created by Dao
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Player> getPlayerDaoList() throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Player player = null;
		List<Player> playerReportListDao = new ArrayList<Player>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s", tableName);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);

			while (resultSet.next()) {
				player = new Player();
				player.setId(resultSet.getInt(Fields.ID.name()));
				player.setFirstName(resultSet.getString(Fields.FIRSTNAME.name()));
				player.setLastName(resultSet.getString(Fields.LASTNAME.name()));
				player.setEmailAddress(resultSet.getString(Fields.EMAILADDRESS.name()));
				player.setBirthDate(resultSet.getString(Fields.BIRTHDATE.name()));
				playerReportListDao.add(player);
			}
		} finally {
			close(statement);
		}
		return playerReportListDao;
	}

	/**
	 * Updates a player
	 * 
	 * @param player
	 *            to be updated
	 * @throws ApplicationException
	 */
	public void update(Player player) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s=%d, %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s=%d", tableName, //
					Fields.ID, player.getId(), //
					Fields.FIRSTNAME, player.getFirstName(), //
					Fields.LASTNAME, player.getLastName(), //
					Fields.EMAILADDRESS, player.getEmailAddress(), //
					Fields.BIRTHDATE, player.getBirthDate(), //
					Fields.ID, player.getId()); //
			System.out.println(sqlString);
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Deletes the player from the table
	 * 
	 * @param player
	 *            to be deleted
	 */
	public void delete(Player player) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s=%d", tableName, Fields.ID, player.getId());
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

		ID(1), FIRSTNAME(2), LASTNAME(3), EMAILADDRESS(4), BIRTHDATE(5);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}
}
