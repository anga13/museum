package se.kth.iv1351.grupp23.museum;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class GUI {
	private static final Object CONFIRM_DELETE_MESSAGE = "Are you sure you want to delete?";

	private Guide selectedGuide;

	private LanguageSkill[] skills;
	private DbHandler db = new DbHandler();

	private LanguageSkill selectedSkill;

	private JFrame frame;

	private JButton addLangSkillButton;

	private JButton deleteLangSkillButton;

	private JList<LanguageSkill> langList;

	private GUI() {
		db.connect();

		List<Guide> allGuides = db.findAllGuides();
		Guide[] guides = new Guide[allGuides.size()];
		guides = allGuides.toArray(guides);

		frame = new JFrame("Guider");
		JPanel mainPanel = new JPanel(new GridLayout(3, 1));
		JPanel guidePanel = new JPanel();
		JPanel tourPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		addLangSkillButton = new JButton("Add");
		deleteLangSkillButton = new JButton("Delete");
		JList<Guide> guideList = new JList<>(guides);
		langList = new JList<>();

		guideList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		guideList.addListSelectionListener(e -> {
			selectedGuide = guideList.getSelectedValue();
			loadLanguageList();
		});
		langList.addListSelectionListener(e -> {
			selectedSkill = langList.getSelectedValue();
			deleteLangSkillButton.setEnabled(true);
			System.out.println(selectedSkill);
		});
		langList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		addLangSkillButton.addActionListener(e -> {
			addLanguageSkill(selectedGuide);
		});

		deleteLangSkillButton.addActionListener(e -> {
			deleteLanguage(selectedGuide, selectedSkill);
		});

		buttonPanel.add(addLangSkillButton);
		buttonPanel.add(deleteLangSkillButton);
		guidePanel.add(guideList);
		tourPanel.add(langList);
		mainPanel.add(guidePanel);
		mainPanel.add(tourPanel);
		mainPanel.add(buttonPanel);
		frame.add(mainPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void loadLanguageList() {
		List<LanguageSkill> skillz = db.GetLanguageSkills(selectedGuide);
		skills = new LanguageSkill[skillz.size()];
		skills = skillz.toArray(skills);
		langList.setListData(skills);
		addLangSkillButton.setEnabled(true);
		deleteLangSkillButton.setEnabled(false);
	}

	private void addLanguageSkill(Guide guide) {
		String newLang = JOptionPane.showInputDialog("Hi");
		System.out.println(newLang);
	}

	private void deleteLanguage(Guide guide, LanguageSkill langSkill) {
		boolean delete = (JOptionPane.showConfirmDialog(frame, CONFIRM_DELETE_MESSAGE) == JOptionPane.YES_OPTION);
		if (delete) {
			db.deleteLanguageSkill(guide, langSkill);
			loadLanguageList();
		}
	}

	public static void create() {
		SwingUtilities.invokeLater(() -> {
			new GUI();
		});
	}
}
