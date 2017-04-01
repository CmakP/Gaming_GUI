package a00977249.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.dao.PersonaDao;
import a00977249.data.Persona;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class PersonaDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger(PersonaDialog.class);

	private Persona persona;

	private final JPanel contentPanel = new JPanel();
	private JTextField txtId;
	private JTextField txtPlayerId;
	private JTextField txtGamertag;
	private JTextField txtPlatform;

	/**
	 * Create the dialog.
	 * 
	 * @throws Exception
	 */
	public PersonaDialog(PersonaDao personaDao, int index) throws Exception {

		persona = personaDao.get(index);

		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][]"));
		{
			JLabel lblId = new JLabel("Id");
			contentPanel.add(lblId, "cell 0 0,alignx trailing");
		}
		{
			txtId = new JTextField();
			txtId.setEnabled(false);
			txtId.setEditable(false);
			contentPanel.add(txtId, "cell 1 0,growx");
			txtId.setColumns(10);
		}
		{
			JLabel lblPlayerrId = new JLabel("Player Id");
			contentPanel.add(lblPlayerrId, "cell 0 1");
		}
		{
			txtPlayerId = new JTextField();
			txtPlayerId.setEditable(false);
			txtPlayerId.setEnabled(false);
			contentPanel.add(txtPlayerId, "cell 1 1,growx");
			txtPlayerId.setColumns(10);
		}
		{
			JLabel lblGamertag = new JLabel("Gamertag");
			contentPanel.add(lblGamertag, "cell 0 2,alignx trailing");
		}
		{
			txtGamertag = new JTextField();
			contentPanel.add(txtGamertag, "cell 1 2,growx");
			txtGamertag.setColumns(10);
		}
		{
			JLabel lblPlatform = new JLabel("Platform");
			contentPanel.add(lblPlatform, "cell 0 3,alignx trailing");
		}
		{
			txtPlatform = new JTextField();
			contentPanel.add(txtPlatform, "cell 1 3,growx,aligny top");
			txtPlatform.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						persona.setGamerTag(txtGamertag.getText());
						persona.setPlatForm(txtPlatform.getText());
						try {
							personaDao.update(persona);
						} catch (SQLException ex) {
							String error = ex.getMessage();
							JOptionPane.showMessageDialog(PersonaDialog.this, error, "Error", JOptionPane.INFORMATION_MESSAGE);
							LOG.error(error);
						}
						PersonaDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PersonaDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setPersonaData();
	}

	private void setPersonaData() {
		txtId.setText(String.valueOf(persona.getId()));
		txtPlayerId.setText(String.valueOf(persona.getPlayerId()));
		txtGamertag.setText(persona.getGamerTag());
		txtPlatform.setText(persona.getPlatForm());
	}
}
