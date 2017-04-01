package a00977249.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import a00977249.data.DataManager;
import a00977249.data.GisOptions;
import a00977249.data.Player;
import a00977249.data.jointtable.PersonaScore;
import a00977249.data.jointtable.PlayerScore;
import a00977249.data.jointtable.sorters.PersonaScoreSorters;
import a00977249.io.DataReport;
import net.miginfocom.swing.MigLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainFrame(DataManager dataManager) {

		GisOptions gisOptions = new GisOptions();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.setMnemonic(KeyEvent.VK_Q);
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmQuit);

		JMenu mnLists = new JMenu("Lists");
		mnLists.setMnemonic(KeyEvent.VK_L);
		menuBar.add(mnLists);

		JMenuItem mntmPlayer = new JMenuItem("Players");
		mntmPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					PlayersDialogList dialog = new PlayersDialogList(dataManager.getPlayerDao());
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnLists.add(mntmPlayer);

		JMenuItem mntmPersonas = new JMenuItem("Personas");
		mntmPersonas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					PersonasDialogList dialog = new PersonasDialogList(dataManager.getPersonaDao());
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnLists.add(mntmPersonas);

		JMenuItem mntmScores = new JMenuItem("Scores");
		mntmScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<PersonaScore> leaderBoardDaoList = null;
				try {
					leaderBoardDaoList = dataManager.getPersonaScoreDao().getPersonaScoreList();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				String[] reportList = DataReport.toArray(leaderBoardDaoList);
				showReportDialog(reportList, false);
			}
		});
		mnLists.add(mntmScores);

		JMenu mnReports = new JMenu("Reports");
		mnReports.setMnemonic(KeyEvent.VK_R);
		menuBar.add(mnReports);

		JMenuItem mntmTotal = new JMenuItem("Total");
		mntmTotal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					String[] results = dataManager.getGameCountsDao().getResults();
					JOptionPane.showMessageDialog(MainFrame.this, results, "Total Reports", JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
		mnReports.add(mntmTotal);

		JCheckBox chckbxDescending = new JCheckBox("Descending");
		chckbxDescending.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxDescending.isSelected()) {
					gisOptions.setDesc(true);
				} else {
					gisOptions.setDesc(false);
				}
			}
		});
		mnReports.add(chckbxDescending);

		JMenuItem mntmByGame = new JMenuItem("By Game");
		mntmByGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<PersonaScore> leaderBoardList = dataManager.getLeaderBoardList();
				Collections.sort(leaderBoardList, new PersonaScoreSorters.CompareByGame());
				if (gisOptions.isDesc()) {
					Collections.reverse(leaderBoardList);
				}
				String[] reportList = DataReport.toArray(leaderBoardList);
				showReportDialog(reportList, false);
			}
		});
		mnReports.add(mntmByGame);

		JMenuItem mntmByCount = new JMenuItem("By Count");
		mntmByCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<PersonaScore> leaderBoardList = dataManager.getLeaderBoardList();
				Collections.sort(leaderBoardList, new PersonaScoreSorters.CompareByCount());
				if (gisOptions.isDesc()) {
					Collections.reverse(leaderBoardList);
				}
				String[] reportList = DataReport.toArray(leaderBoardList);
				showReportDialog(reportList, false);
			}
		});
		mnReports.add(mntmByCount);

		JMenuItem mntmGamertag = new JMenuItem("Gamertag");
		mntmGamertag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					String gamertagFilter = JOptionPane.showInputDialog(MainFrame.this, "Enter a Gamertag:", "Filter Score List by Gamertag", JOptionPane.INFORMATION_MESSAGE);
					String[] gamertags = dataManager.getPersonaDao().getGamertags();
					int i = 0;
					boolean searching = true;
					while (searching && i < gamertags.length) {
						if (gamertags[i].toLowerCase().equals(gamertagFilter.toLowerCase())) {
							searching = false;
							List<PersonaScore> list = dataManager.getLeaderBoardList();
							List<PersonaScore> filteredList = new PersonaScoreSorters.CompareByGamertag().filterGamerTag(list, gamertagFilter);
							if (gisOptions.isDesc()) {
								Collections.reverse(filteredList);
							}
							String[] reportList = DataReport.toArray(filteredList);
							showReportDialog(reportList, false);
						}
						i++;
					}
					if (searching) {
						JOptionPane.showMessageDialog(MainFrame.this, "Gamertag filter '" + gamertagFilter + "' not found in the database\n" + "Enter a valid gamertag filter",
								"Error", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnReports.add(mntmGamertag);

		JMenuItem mntmPlayerReport = new JMenuItem("Player Report");
		mntmPlayerReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<Integer, PlayerScore> playerReportDaoMap;
				try {
					playerReportDaoMap = dataManager.getPlayerScoreDao().getPlayerReportDaoMap();
					List<Player> playerListDao = dataManager.getPlayerDao().getPlayerDaoList();
					String[] reportList = DataReport.toArray(playerListDao, playerReportDaoMap);
					showReportDialog(reportList, true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnReports.add(mntmPlayerReport);

		JMenu mnPlatform = new JMenu("By Platforms");
		mnPlatform.addMenuListener(new MenuListener() {
			
			// All unimplemented actions have to be performed
			public void menuCanceled(MenuEvent arg0) {
			}

			public void menuDeselected(MenuEvent arg0) {
				// Removing all menu items from the menu as they are being added dynamically
				// to prevent duplications
				mnPlatform.removeAll();
			}

			// Menu items are added dynamically
			public void menuSelected(MenuEvent arg0) {
				List<String> platforms;
				try {
					platforms = dataManager.getPersonaDao().getPlatforms();
					for (String platform : platforms) {
						JMenuItem menuItem = new JMenuItem(platform);
						mnPlatform.add(menuItem);
						menuItem.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								// Prints which of item was clicked from the menu
								System.out.println(">>>>>>>>>> " + e.getActionCommand());
							}
						});
						// Complete the action listener below according to the chosen platform 
						// mnPlatform.add(new JMenuItem(platform).addActionListener(new ActionListener()
						// {public void actionPerformed(ActionEvent e) {
						//
						// }
						// }));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		mnReports.add(mnPlatform);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				JOptionPane.showMessageDialog(MainFrame.this, "This program is core of a Games Information System (GIS)\n" + "By Siamak Pourian", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mntmAbout.setMnemonic(KeyEvent.VK_F1);
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[]", "[]"));
	}

	private void showReportDialog(String[] leaderBoardList, boolean headerLabel) {
		try {
			GisReportList dialog = new GisReportList(leaderBoardList, headerLabel);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
