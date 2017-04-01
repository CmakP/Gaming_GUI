/**
 * Project: A00977249Assignment2
 * File: Score.java
 * Date: Jun 20, 2016
 * Time: 9:03:36 PM
 */
package a00977249.data;

/**
 * @author Siamak Pourian, A009772249
 *
 *         Score Class
 */
public class Score {

	public static final String SCORE_DATA_FORMAT = "PERSONA_ID|GAME_ID|WIN";
	public static final int NUMBER_OF_SCORE_DATA_ELEMENTS = 3;

	private int scoreId; // to be assigned as primary key in the table
	private int personaId;
	private String gameId;
	private boolean win;

	/**
	 * Zero-Parameter Constructor
	 */
	public Score() {
	}

	/**
	 * Overloaded Constructor
	 */
	public Score(int scoreId, int personaId, String gameId, boolean win) {
		this.scoreId = scoreId;
		this.personaId = personaId;
		this.gameId = gameId;
		this.win = win;
	}

	/**
	 * @return the personaId
	 */
	public int getPersonaId() {
		return personaId;
	}

	/**
	 * @param personaId
	 *            the personaId to set
	 */
	public void setPersonaId(int personaId) {
		this.personaId = personaId;
	}

	/**
	 * @return the gameId
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * @param gameId
	 *            the gameId to set
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return the win as String
	 */
	public String getWinToString() {
		return win == true ? "win" : "lose";
	}

	/**
	 * Sets the win based on the input String
	 * 
	 * @param win
	 *            the input String
	 */
	public void setWinToBoolean(String win) {
		if (win.trim().toLowerCase().equals("win")) {
			this.win = true;
		} else if (win.trim().toLowerCase().equals("lose")) {
			this.win = false;
		}
	}

	/**
	 * @return the win
	 */
	public boolean getWin() {
		return win;
	}

	/**
	 * @param win
	 *            the win to set
	 */
	public void setWin(boolean win) {
		this.win = win;
	}

	/**
	 * @return the scoreId
	 */
	public int getScoreId() {
		return scoreId;
	}

	/**
	 * @param scoreId
	 *            the scoreId to set
	 */
	public void setScoreId(int scoreId) {
		this.scoreId = scoreId;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Score [scoreId=" + scoreId + ", personaId=" + personaId + ", gameId=" + gameId + ", win=" + win + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + personaId;
		result = prime * result + scoreId;
		result = prime * result + (win ? 1231 : 1237);
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
		if (!(obj instanceof Score)) {
			return false;
		}
		Score other = (Score) obj;
		if (gameId == null) {
			if (other.gameId != null) {
				return false;
			}
		} else if (!gameId.equals(other.gameId)) {
			return false;
		}
		if (personaId != other.personaId) {
			return false;
		}
		if (scoreId != other.scoreId) {
			return false;
		}
		if (win != other.win) {
			return false;
		}
		return true;
	}
}
