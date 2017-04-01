package a00977249.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00977249.ApplicationException;
import a00977249.dao.PlayerDao;
import a00977249.data.Player;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger(PlayerDialog.class);

	private final JPanel contentPanel = new JPanel();
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtEmail;
	private JTextField txtBirthdate;
	private JTextField txtId;

	private Player player;

	/**
	 * Create the dialog.
	 * 
	 * @throws Exception
	 */
	public PlayerDialog(PlayerDao playerDao, int index) throws Exception {

		player = playerDao.get(index);

		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][][][][][][]"));
		{
			JLabel lblId = new JLabel("ID");
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
			JLabel lblFirstName = new JLabel("First Name");
			contentPanel.add(lblFirstName, "cell 0 2,alignx trailing");
		}
		{
			txtFirstName = new JTextField();
			contentPanel.add(txtFirstName, "cell 1 2,growx,aligny top");
			txtFirstName.setColumns(10);
		}
		{
			JLabel lblLastName = new JLabel("Last Name");
			contentPanel.add(lblLastName, "cell 0 4,alignx trailing");
		}
		{
			txtLastName = new JTextField();
			contentPanel.add(txtLastName, "cell 1 4,growx");
			txtLastName.setColumns(10);
		}
		{
			JLabel lblEnailAddress = new JLabel("Email Address");
			contentPanel.add(lblEnailAddress, "cell 0 6,alignx trailing");
		}
		{
			txtEmail = new JTextField(player.getEmailAddress());
			contentPanel.add(txtEmail, "cell 1 6,growx");
			txtEmail.setColumns(10);
		}
		{
			JLabel lblBirthdate = new JLabel("Birthdate");
			contentPanel.add(lblBirthdate, "cell 0 8,alignx trailing");
		}
		{
			txtBirthdate = new JTextField();
			contentPanel.add(txtBirthdate, "cell 1 8,growx");
			txtBirthdate.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						player.setFirstName(txtFirstName.getText());
						player.setLastName(txtLastName.getText());
						try {
							player.setEmailAddress(txtEmail.getText());
							player.setBirthDate(txtBirthdate.getText());
							playerDao.update(player);
						} catch (ApplicationException | SQLException ex) {
							String error = ex.getMessage();
							JOptionPane.showMessageDialog(PlayerDialog.this, error, "Error", JOptionPane.INFORMATION_MESSAGE);
							LOG.error(error);
						}
						PlayerDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						PlayerDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setPlayerData();
	}

	private void setPlayerData() {
		txtId.setText(String.valueOf(player.getId()));
		txtFirstName.setText(player.getFirstName());
		txtLastName.setText(player.getLastName());
		txtEmail.setText(player.getEmailAddress());
		txtBirthdate.setText(player.getBirthDate().toString());
	}
}
