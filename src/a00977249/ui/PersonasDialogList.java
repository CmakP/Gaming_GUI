package a00977249.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JList;

import a00977249.dao.PersonaDao;

import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class PersonasDialogList extends JDialog {

	private AbstractList listModel;

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 * 
	 * @throws SQLException
	 */
	public PersonasDialogList(PersonaDao personaDao) throws SQLException {

		this.listModel = new AbstractList();

		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow]", "[][grow]"));
		{
			{
				JLabel lblSelectAgamertag = new JLabel("Select a 'gamertag'");
				contentPanel.add(lblSelectAgamertag, "cell 2 0");
			}

			String[] gamerTags = personaDao.getGamertags();
			for (String gamertag : gamerTags) {
				listModel.addElement(gamertag);
			}
			@SuppressWarnings("rawtypes")
			JList list = new JList<String>(listModel);

			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// Get selected indices
					int index = list.getSelectedIndex();

					try {
						PersonaDialog dialog = new PersonaDialog(personaDao, index + 1);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);

						// update the list again

						String[] personas = personaDao.getGamertags();
						String updatedPersona = personas[index];
						if (updatedPersona != null) {
							if (index == -1) {
								listModel.addElement(updatedPersona);
							} else {
								listModel.remove(index);
								listModel.add(index, updatedPersona);
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				};
			});
			contentPanel.add(new JScrollPane(list), "cell 2 1,grow");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PersonasDialogList.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
