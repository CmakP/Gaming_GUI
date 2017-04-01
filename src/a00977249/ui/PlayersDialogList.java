package a00977249.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import a00977249.dao.PlayerDao;
import net.miginfocom.swing.MigLayout;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class PlayersDialogList extends JDialog {

	private AbstractList listModel;

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 * 
	 * @throws SQLException
	 */
	public PlayersDialogList(PlayerDao playerDao) throws SQLException {

		this.listModel = new AbstractList();

		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow]", "[grow]"));
		
		{
			String[] firstLastNames = playerDao.getFirstLastName();
			for (String firstLastName : firstLastNames) {
				listModel.addElement(firstLastName);
			}

			JList<String> list = new JList<String>(listModel);

			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// Get selected indices
					int index = list.getSelectedIndex();

					try {
						PlayerDialog dialog = new PlayerDialog(playerDao, index + 1);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);

						// update the list again

						String[] players = playerDao.getFirstLastName();
						String updatedPlayer = players[index];
						if (updatedPlayer != null) {
							if (index == -1) {
								listModel.addElement(updatedPlayer);
							} else {
						//		listModel.remove(index);
						//		listModel.add(index, updatedPlayer);
								listModel.set(index, updatedPlayer);
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			contentPanel.add(new JScrollPane(list), "cell 2 0,grow");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PlayersDialogList.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
