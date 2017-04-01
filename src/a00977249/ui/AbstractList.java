/**
 * Project: A00977249Assignment2
 * File: AbstractList.java
 * Date: Jul 1, 2016
 * Time: 12:53:15 PM
 */
package a00977249.ui;

import javax.swing.DefaultListModel;

/**
 * @author Siamak Pourian, A009772249
 *
 *         AbstractList Class
 */
@SuppressWarnings("serial")
public class AbstractList extends DefaultListModel<String> {

	public AbstractList() {
	}

	@Override
	public void addElement(String player) {
		super.addElement(player);
	}

}
