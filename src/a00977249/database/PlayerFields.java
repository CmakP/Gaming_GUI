/**
 * Project: A00977249Assignment2
 * File: PlayerFields.java
 * Date: Jun 20, 2016
 * Time: 8:47:30 PM
 */
package a00977249.database;

/**
 * @author Siamak Pourian, A009772249
 *
 *         PlayerFields Enum
 */
public enum PlayerFields {

	ID(1), FIRSTNAME(2), LASTNAME(3), EMAILADDRESS(4), GAMERTAG(5), BIRTHDATE(6);

	private final int column;

	PlayerFields(int column) {
		this.column = column;
	}

	public int getColumn() {
		return column;
	}
}
