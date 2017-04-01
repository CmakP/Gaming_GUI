package a00977249.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GisReportList extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public GisReportList(String[] filteredList, boolean lblFlag) {
		setBounds(100, 100, 646, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		{
			if (!lblFlag) {
				JLabel lblWinloss = new JLabel("Win:Loss       Game Name             Gamertag          Platform");
				contentPanel.add(lblWinloss, "flowx,cell 0 0");
			} else {
				JLabel lblWinloss = new JLabel("Player Id   Full Name                     Email                           Birthdate       TotalGamePlay      Total  Win");
				contentPanel.add(lblWinloss, "flowx,cell 0 0");
			}
		}
		{
			@SuppressWarnings({ "unchecked", "rawtypes" })
			JList list = new JList(filteredList);
			contentPanel.add(new JScrollPane(list), "cell 0 1,grow");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GisReportList.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
