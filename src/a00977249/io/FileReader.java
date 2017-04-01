/**
 * Project: A00977249Assignment2
 * File: FileReader.java
 * Date: Jun 23, 2016
 * Time: 1:01:50 PM
 */
package a00977249.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.ApplicationException;
import a00977249.Gis;

/**
 * @author Siamak Pourian, A009772249
 *
 *         FileReader Class
 */
public class FileReader {

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	/**
	 * Reads the content of a file
	 * 
	 * @param file
	 *            the file to be read
	 * @param dataFormat
	 *            the format of the data in this file
	 * @return a String array of the files content
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	public static List<String> read(File file, String fileDataFormat) throws ApplicationException, FileNotFoundException {
		List<String> fileContent = new ArrayList<String>();
		if (!file.exists()) {
			throw new FileNotFoundException(String.format("\nFile %s doesn't exist in the source directory. Try adding it to the src path.", file.getName()));
		}
		LOG.debug(file.getAbsolutePath());
		Scanner scanner = new Scanner(file);
		if (!scanner.nextLine().equals(fileDataFormat)) {
			throw new ApplicationException(String.format("Format mismatch in %s file.", file.getName()));
		}
		while (scanner.hasNext()) {
			String row = scanner.nextLine();
			fileContent.add(row);
		}
		scanner.close();
		return fileContent;
	}
}
