/**
 * Project: A00977249Assignment2
 * File: TableSize.java
 * Date: Jun 27, 2016
 * Time: 9:59:00 PM
 */
package a00977249.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import a00977249.database.Database;

/**
 * @author Siamak Pourian, A009772249
 *
 *         TableSize Class
 */
public class TableSize {

	/**
	 * Returns the size of table
	 */
	public static int getTableSize(Database database, String tableName) throws SQLException {
		Connection connection = database.getConnection();
		Statement statement = connection.createStatement();
		String sqlString = String.format("SELECT * FROM %s", tableName);
		ResultSet resultSet = statement.executeQuery(sqlString);
		int count = 0;
		while (resultSet.next())
			count++;
		return count;
	}
}
