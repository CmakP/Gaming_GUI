/**
 * Project: A00977249Assignment2
 * File: Players.java
 * Date: Jun 20, 2016
 * Time: 8:41:57 PM
 */
package a00977249.data;

import java.sql.Date;

import a00977249.ApplicationException;
import a00977249.util.Validator;

public class Player {

	public static final String PLAYER_DATA_FORMAT = "ID|FIRST_NAME|LAST_NAME|EMAIL|BIRTHDATE";
	public static final int NUMBER_OF_PLAYER_DATA_ELEMENTS = 5;

	private int id;
	private String firstName;
	private String lastName;
	private String emailAddress;

	private Date birthDate;

	/**
	 * Zero-Parameter Constructor
	 */
	public Player() {
	}

	/**
	 * Overloaded Constructor
	 */
	public Player(int id, String firstName, String lastName, String emailAddress, Date birthDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.birthDate = birthDate;
	}

	/**
	 * Overloaded Constructor
	 * 
	 * @throws ApplicationException
	 */
	public Player(int id, String firstName, String lastName, String emailAddress, String birthDate) throws ApplicationException {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		setBirthDate(birthDate);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) throws ApplicationException {
		if (!Validator.validateEmail(emailAddress)) {
			throw new ApplicationException("Invalid email address element: " + emailAddress);
		}
		this.emailAddress = emailAddress;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public void setBirthDate(String birthdate) {
		this.birthDate = Date.valueOf(birthdate);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress=" + emailAddress + ", birthDate=" + birthDate + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Player)) {
			return false;
		}
		Player other = (Player) obj;
		if (birthDate == null) {
			if (other.birthDate != null) {
				return false;
			}
		} else if (!birthDate.equals(other.birthDate)) {
			return false;
		}
		if (emailAddress == null) {
			if (other.emailAddress != null) {
				return false;
			}
		} else if (!emailAddress.equals(other.emailAddress)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		return true;
	}
}
