/**
 * Project: A00977249Assignment2
 * File: EmailValidator.java
 * Date: Jun 20, 2016
 * Time: 8:40:07 PM
 */
package a00977249.util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * @author Siamak Pourian, A009772249
 *
 *         EmailValidator Class
 */
public class Validator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

	/**
	 * Zero-Parameter Constructor
	 */
	private Validator() {
	}

	/**
	 * Validate an email string.
	 * 
	 * @param email
	 *            the email string.
	 * @return true if the email address is valid and false otherwise
	 */
	public static boolean validateEmail(final String email) {
		if (emailPattern == null) {
			emailPattern = Pattern.compile(EMAIL_PATTERN);
		}
		return emailPattern.matcher(email).matches();
	}

	/**
	 * 
	 * @param birthdate
	 *            the date to be validated
	 * @return true if the birthdate is valid and false otherwise
	 */
	public static boolean validateBirthdate(final Date birthdate) {
		LocalDate date = birthdate.toLocalDate();
		if (date.getYear() < 1900 || date.getYear() > 2016) {
			return false;
		} else if (date.getMonthValue() < 01 || date.getMonthValue() > 12) {
			return false;
		} else if (date.getDayOfMonth() < 01 || date.getDayOfMonth() > 31) {
			return false;
		}
		return true;
	}
}
