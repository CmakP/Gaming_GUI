/**
 * Project: A00977249Assignment2
 * File: PersonaDao.java
 * Date: Jun 23, 2016
 * Time: 2:28:38 PM
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
import a00977249.data.Persona;
import a00977249.database.Database;

/**
 * @author Siamak Pourian, A009772249
 *
 *         PersonaDao Class
 */
public class PersonaDao extends Dao {

	public static final String TABLE_NAME = "Persona";

	private static final Logger LOG = LogManager.getLogger(PersonaDao.class);

	/**
	 * Overloaded Constructor
	 */
	public PersonaDao(Database database) {
		super(database, TABLE_NAME);
	}

	/**
	 * Creates the Persona table and the initializes the SQL statement
	 */
	@Override
	public void create() throws SQLException {
		String createStatement = String.format("create table %s(%s INTEGER, %s INTEGER, %s VARCHAR(20), %s VARCHAR(5), primary key (%s) )", tableName, //
				Fields.ID, Fields.PLAYERID, Fields.GAMERTAG, Fields.PLATFORM, Fields.ID);
		super.create(createStatement);
	}

	/**
	 * Adds a Persona to the table
	 * 
	 * @param persona
	 *            to be added to the table
	 * @throws SQLException
	 */
	public void add(Persona persona) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values(%d, %d, '%s', '%s')", tableName, //
					persona.getId(), persona.getPlayerId(), persona.getGamerTag(), persona.getPlatForm());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieves an array of gamerTags
	 * 
	 * @return an array of gamerTags
	 * @throws SQLException
	 */
	public String[] getGamertags() throws SQLException {
		String[] gamerTags = new String[TableSize.getTableSize(database, TABLE_NAME)];
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
				gamerTags[index] = resultSet.getString(Fields.GAMERTAG.name());
				index++;
			}
		} finally {
			close(statement);
		}
		return gamerTags;
	}

	/**
	 * Retrieves a Persona using their gamertag
	 * 
	 * @param gamertag
	 *            the search key
	 * @return the Persona matching the key
	 * @throws Exception
	 */
	public Persona get(int id) throws Exception {
		Connection connection;
		Statement statement = null;
		Persona persona = null;

		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", tableName, Fields.ID, id);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get Persona
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}
				persona = new Persona();
				persona.setId(resultSet.getInt(Fields.ID.name()));
				persona.setPlayerId(resultSet.getInt(Fields.PLAYERID.name()));
				persona.setGamerTag(resultSet.getString(Fields.GAMERTAG.name()));
				persona.setPlatForm(resultSet.getString(Fields.PLATFORM.name()));
			}
		} finally {
			close(statement);
		}
		return persona;
	}

	/**
	 * Retrieves a Persona using their gamertag
	 * 
	 * @param gamertag
	 *            the search key
	 * @return the Persona matching the key
	 * @throws Exception
	 */
	public Persona getGamertag(String gamertag) throws Exception {
		Connection connection;
		Statement statement = null;
		Persona persona = null;

		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.GAMERTAG, gamertag);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get Persona
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}
				persona = new Persona();
				persona.setId(resultSet.getInt(Fields.ID.name()));
				persona.setPlayerId(resultSet.getInt(Fields.PLAYERID.name()));
				persona.setGamerTag(resultSet.getString(Fields.GAMERTAG.name()));
				persona.setPlatForm(resultSet.getString(Fields.PLATFORM.name()));
			}
		} finally {
			close(statement);
		}
		return persona;
	}

	/**
	 * Retrieves All platforms
	 * 
	 * @return a collection of Platforms
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<String> getPlatforms() throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		String platform;
		List<String> platforms = new ArrayList<String>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s", tableName);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);
			while (resultSet.next()) {
				platform = resultSet.getString(Fields.PLATFORM.name());
				if (!platforms.contains(platform)) {
					platforms.add(platform);
				}
			}
		} finally {
			close(statement);
		}
		return platforms;
	}

	/**
	 * Updates Persona
	 * 
	 * @param persona
	 *            to be updated
	 */
	public void update(Persona persona) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s=%d, %s=%d, %s='%s', %s='%s' WHERE %s=%d", tableName, //
					Fields.ID, persona.getId(), //
					Fields.PLAYERID, persona.getPlayerId(), //
					Fields.GAMERTAG, persona.getGamerTag(), //
					Fields.PLATFORM, persona.getPlatForm(), //
					Fields.ID, persona.getId()); //
			System.out.println(sqlString);
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Deletes the Persona from the table
	 * 
	 * @param persona
	 *            to be deleted
	 */
	public void delete(Persona persona) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s=%d", tableName, Fields.ID, persona.getId());
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

		ID(1), PLAYERID(2), GAMERTAG(3), PLATFORM(4);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}
}
