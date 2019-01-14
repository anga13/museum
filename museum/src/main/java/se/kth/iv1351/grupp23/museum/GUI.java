package se.kth.iv1351.grupp23.museum;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class GUI {
	private static final Object CONFIRM_DELETE_MESSAGE = "Are you sure you want to delete?";

	private Guide selectedGuide;

	private DbHandler db;

	private LanguageSkill selectedSkill;

	private JFrame frame;

	private JButton addLangSkillButton;

	private JButton deleteLangSkillButton;

	private JList<LanguageSkill> langList;

	private AddLangPanel addLangPanel;

	private GUI() {
		db = new DbHandler();
		db.connect();

		List<Guide> allGuides = db.findAllGuides();
		Guide[] guides = new Guide[allGuides.size()];
		guides = allGuides.toArray(guides);

		frame = new JFrame("Guider");
		JPanel mainPanel = new JPanel(new GridLayout(4, 1));
		JPanel guidePanel = new JPanel();
		JPanel tourPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		addLangPanel = new AddLangPanel();
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
		mainPanel.add(addLangPanel);
		addLangPanel.setVisible(false);
		frame.add(mainPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void loadLanguageList() {
		List<LanguageSkill> skills = db.GetLanguageSkills(selectedGuide);
		LanguageSkill[] skillsArray = new LanguageSkill[skills.size()];
		skillsArray = skills.toArray(skillsArray);
		langList.setListData(skillsArray);
		addLangSkillButton.setEnabled(true);
		deleteLangSkillButton.setEnabled(false);
	}

	private void addLanguageSkill(Guide guide) {
		addLangPanel.setGuide(guide);
		addLangPanel.setVisible(true);
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

	@SuppressWarnings("serial")
	private class AddLangPanel extends JPanel {
		private JTextField langLevelField;
		private Guide guide;
		private List<String> languages;
		private JComboBox<String> langBox;

		public AddLangPanel() {
			languages = db.findAllLanguages();
			langLevelField = new JTextField(20);
			JButton confirmButton = new JButton("Create");
			JButton cancelButton = new JButton("Cancel");
			String[] langArray = languages.toArray(new String[languages.size()]);
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(langArray);
			langBox = new JComboBox<>(model);
			langBox.setSelectedIndex(0);
			confirmButton.addActionListener(e -> {
				String language = (String) langBox.getSelectedItem();
				String level = langLevelField.getText();
				LanguageSkill skill = new LanguageSkill(language, level);
				db.createNewLanguageSkill(guide, skill);
				GUI.this.loadLanguageList();
				this.setVisible(false);
			});
			cancelButton.addActionListener(e -> {
				langLevelField.setText("");
				this.setVisible(false);
			});
			this.add(langBox);
			this.add(langLevelField);
			this.add(confirmButton);
			this.add(cancelButton);
		}

		public void setGuide(Guide guide) {
			languages = db.findUnregistredLanguages(guide);
			String[] langArray = languages.toArray(new String[languages.size()]);
			ComboBoxModel<String> model = new DefaultComboBoxModel<>(langArray);
			langBox.setModel(model);
		}
	}
}
