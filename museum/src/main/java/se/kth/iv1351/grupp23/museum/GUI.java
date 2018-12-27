package se.kth.iv1351.grupp23.museum;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class GUI {
	private Guide selectedGuide;

	private LanguageSkill[] skills;
	private DbHandler db = new DbHandler();
	
	private GUI() {
		db.connect();
		
		List<Guide> allGuides = db.findAllGuides();
		Guide[] guides = new Guide[allGuides.size()];
		guides = allGuides.toArray(guides);
		JFrame frame = new JFrame("Guider");
		JPanel mainPanel = new JPanel(new GridLayout(2, 1));
		JPanel guidePanel = new JPanel();
		JPanel tourPanel = new JPanel();
		JList<Guide> guideList = new JList<>(guides);
		JList<LanguageSkill> langList = new JList<>();
		guideList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		guideList.addListSelectionListener(e -> {
			selectedGuide = guideList.getSelectedValue();
			skills = toggleList();
			langList.setListData(skills);
		});

		guidePanel.add(guideList);
		tourPanel.add(langList);
		mainPanel.add(guidePanel);
		mainPanel.add(tourPanel);
		frame.add(mainPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private LanguageSkill[] toggleList() {
		List<LanguageSkill> skillz = db.GetLanguageSkills(selectedGuide);
		LanguageSkill[] skillzz = new LanguageSkill[skillz.size()];
		skillzz = skillz.toArray(skillzz);
		return skillzz;
	}

	public static void create() {
		SwingUtilities.invokeLater(() -> {
			new GUI();
		});
	}
}
