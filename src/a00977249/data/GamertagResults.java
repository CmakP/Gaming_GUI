/**
 * Project: A00977249Assignment2
 * File: GamertagResults.java
 * Date: Jun 28, 2016
 * Time: 1:48:28 PM
 */
package a00977249.data;

/**
 * @author Siamak Pourian, A009772249
 *
 *         GamertagResults Class
 */
public class GamertagResults {

	private String gameName;
	private int totalPlayed;

	/**
	 * 
	 */
	public GamertagResults() {
		this.gameName = null;
		this.totalPlayed = 0;
	}

	/**
	 * 
	 */
	public GamertagResults(String gameName, int totalPlayed) {
		this.gameName = gameName;
		this.totalPlayed = totalPlayed;
	}

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName
	 *            the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the totalPlayed
	 */
	public int getTotalPlayed() {
		return totalPlayed;
	}

	/**
	 * @param totalPlayed
	 *            the totalPlayed to set
	 */
	public void setTotalPlayed(int totalPlayed) {
		this.totalPlayed = totalPlayed;
	}
}
