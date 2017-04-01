/**
 * Project: A00977249Assignment2
 * File: Gis.java
 * Date: Jun 20, 2016
 * Time: 8:28:39 PM
 */
package a00977249;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.data.DataManager;
import a00977249.database.Database;
import a00977249.ui.MainFrame;

/**
 * @author Siamak Pourian, A009772249
 *
 *         Gis Class, point of entry to the GIS
 */
public class Gis {

	public static final String DB_PROPERTIES_FILENAME = "db.properties";
	private static final Logger LOG = LogManager.getLogger(Gis.class);

	private static Database database;

	private final Properties dbProperties;
	private Connection connection;

	private static DataManager dataManager;

	/**
	 * Point of entry to the program
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Instant startTime = Instant.now();
		LOG.info(startTime);
		File dbPropertiesFile = new File(DB_PROPERTIES_FILENAME);
		if (!dbPropertiesFile.exists()) {
			showUsage();
			System.exit(-1);
		}

		try {
			new Gis(dbPropertiesFile).run();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {
			database.shutdown();
			Instant endTime = Instant.now();
			LOG.info(endTime);
		}
	}

	/**
	 * Sets the Nimbus look and feel, creates and displays the GIS
	 */
	public static void createUI() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame(dataManager);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Displays an error message for the database properties file
	 */
	private static void showUsage() {
		LOG.info("The database properties filename must be passed in the commandline.");
	}

	/**
	 * Overloaded constructor
	 * 
	 * @param dbPropertiesFile
	 *            The database properties filename
	 * @throws IOException
	 */
	private Gis(File dbPropertiesFile) throws IOException {
		dbProperties = new Properties();
		dbProperties.load(new FileReader(dbPropertiesFile));

		database = new Database(dbProperties);
		dataManager = new DataManager(database);
	}

	/**
	 * The flow of the program
	 * 
	 * @throws Exception
	 */
	private void run() throws Exception {

		try {
			// set the Nimbus look and feel...
			// create and show the user interface
			createUI();
			LOG.info("UI created");

			connection = database.getConnection();  //  Really needed here??? 
			dataManager.loadData();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			connection.close();
		}
	}
}