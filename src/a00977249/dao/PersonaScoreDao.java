/**
 * Project: A00977249Assignment2
 * File: PersonaScoreDao.java
 * Date: Jun 25, 2016
 * Time: 9:58:59 AM
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

import a00977249.data.jointtable.PersonaScore;
import a00977249.database.Database;

/**
 * @author Siamak Pourian, A009772249
 *
 *         PersonaScoreDao Class
 */
public class PersonaScoreDao extends Dao {

	public static final String TABLE_NAME = "PersonaScore";

	private static final Logger LOG = LogManager.getLogger(PersonaScoreDao.class);

	/**
	 * Overloaded Constructor
	 */
	public PersonaScoreDao(Database database) {
		super(database, TABLE_NAME);
	}

	/**
	 * Creates the PersonaScore table and the initializes the SQL statement
	 */
	@Override
	public void create() throws SQLException {
		String createStatement = String.format(
				"create table %s (%s VARCHAR(15), %s INTEGER, %s INTEGER, %s INTEGER, %s VARCHAR(30), %s VARCHAR(20), %s VARCHAR(5), primary key (%s) )", tableName, //
				Fields.PERSONAIDGAMEID, Fields.PERSONAID, Fields.TOTALWINS, Fields.TOTALLOSS, Fields.GAMENAME, Fields.GAMERTAG, Fields.PLATFORM, Fields.PERSONAIDGAMEID);
		super.create(createStatement);
	}

	/**
	 * Adds a PersonaScore to the table
	 * 
	 * @param personaScore
	 *            to be added to the table
	 * @throws SQLException
	 */
	public void add(PersonaScore personaScore) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values('%s', %d, %d, %d, '%s', '%s', '%s')", tableName, //
					personaScore.getPersonaIdGameId(), personaScore.getPersonaId(), personaScore.getTotalWin(), personaScore.getTotalLoss(), personaScore.getGameName(),
					personaScore.getGamerTag(), personaScore.getPlatForm());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieves personaScore by personaIdGameId
	 * 
	 * @param personaIdGameId
	 *            the search key
	 * @return the PersonaScore matching the key
	 * @throws SQLException
	 * @throws Exception
	 */
	public PersonaScore get(String personaIdGameId) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		PersonaScore personaScore = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s='%s'", tableName, Fields.PERSONAIDGAMEID, personaIdGameId);
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
				personaScore = new PersonaScore();
				personaScore.setPersonaIdGameId(resultSet.getString(Fields.PERSONAIDGAMEID.name()));
				personaScore.setPersonaId(resultSet.getInt(Fields.PERSONAID.name()));
				personaScore.setTotalWin(resultSet.getInt(Fields.TOTALWINS.name()));
				personaScore.setTotalLoss(resultSet.getInt(Fields.TOTALLOSS.name()));
				personaScore.setGameName(resultSet.getString(Fields.GAMENAME.name()));
				personaScore.setGamerTag(resultSet.getString(Fields.GAMERTAG.name()));
				personaScore.setPlatForm(resultSet.getString(Fields.PLATFORM.name()));
			}
		} finally {
			close(statement);
		}
		return personaScore;
	}

	/**
	 * Retrieves personaScore by personaIdGameId
	 * 
	 * @return a PersonaScore collection created by Dao
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<PersonaScore> getPersonaScoreList() throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		PersonaScore personaScore = null;
		List<PersonaScore> peronaScoreDaoList = new ArrayList<PersonaScore>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s", tableName);
			LOG.debug(sqlString);
			ResultSet resultSet = statement.executeQuery(sqlString);

			while (resultSet.next()) {
				personaScore = new PersonaScore();
				personaScore.setPersonaIdGameId(resultSet.getString(Fields.PERSONAIDGAMEID.name()));
				personaScore.setPersonaId(resultSet.getInt(Fields.PERSONAID.name()));
				personaScore.setTotalWin(resultSet.getInt(Fields.TOTALWINS.name()));
				personaScore.setTotalLoss(resultSet.getInt(Fields.TOTALLOSS.name()));
				personaScore.setGameName(resultSet.getString(Fields.GAMENAME.name()));
				personaScore.setGamerTag(resultSet.getString(Fields.GAMERTAG.name()));
				personaScore.setPlatForm(resultSet.getString(Fields.PLATFORM.name()));
				peronaScoreDaoList.add(personaScore);
			}
		} finally {
			close(statement);
		}
		return peronaScoreDaoList;
	}

	/**
	 * Updates a personaScore
	 * 
	 * @param personaScore
	 *            to be updated
	 */
	public void update(PersonaScore personaScore) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s='%s', %s=%d, %s=%d, %s=%d, %s='%s', %s='%s', %s='%s' WHERE %s='%s' ", tableName, //
					Fields.PERSONAIDGAMEID, personaScore.getPersonaIdGameId(), //
					Fields.PERSONAID, personaScore.getPersonaId(), //
					Fields.TOTALWINS, personaScore.getTotalWin(), //
					Fields.TOTALLOSS, personaScore.getTotalLoss(), //
					Fields.GAMERTAG, personaScore.getGamerTag(), //
					Fields.GAMENAME, personaScore.getGameName(), //
					Fields.PLATFORM, personaScore.getPlatForm(), //
					Fields.PERSONAIDGAMEID, personaScore.getPersonaIdGameId()); //

			System.out.println(sqlString);
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Deletes the PersonaScore from the table
	 * 
	 * @param personaScore
	 *            to be deleted
	 */
	public void delete(PersonaScore personaScore) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.PERSONAIDGAMEID, personaScore.getPersonaIdGameId());
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

		PERSONAIDGAMEID(1), PERSONAID(2), TOTALWINS(3), TOTALLOSS(4), GAMENAME(5), GAMERTAG(6), PLATFORM(7);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}
}
