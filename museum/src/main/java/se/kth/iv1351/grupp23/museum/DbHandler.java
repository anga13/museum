package se.kth.iv1351.grupp23.museum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbHandler {
	private Connection conn;// DB connection variable
	// DB access variables
	private String URL = "jdbc:ucanaccess://c:/path/to/access/database/file.accdb";
	private String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
	private String userID = "";
	private String password = "";

	public void connect() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(URL, userID, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Guide> findAllGuides() {
		List<Guide> guides = new ArrayList<>();
		String query = "SELECT personnr, förnamn, efternamn FROM Guide";
		try {
			Statement stmnt = conn.createStatement();
			ResultSet rs = stmnt.executeQuery(query);
			while (rs.next()) {
				String persnr = rs.getString("personnr");
				String enamn = rs.getString("efternamn");
				String fnamn = rs.getString("förnamn");
				guides.add(new Guide(persnr, enamn, fnamn));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return guides;

	}

	public Guide findGuideById(String id) {
		String query = "SELECT * FROM Guide WHERE personnr=?";
		Guide guide = null;
		try {
			PreparedStatement stmnt = conn.prepareStatement(query);
			stmnt.setString(1, id);
			ResultSet rs = stmnt.executeQuery();
			while (rs.next()) {
				String persnr = rs.getString("personnr");
				String enamn = rs.getString("efternamn");
				String fnamn = rs.getString("förnamn");
				guide = new Guide(persnr, enamn, fnamn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return guide;
	}

	public List<LanguageSkill> GetLanguageSkills(Guide guide) {
		String query = "SELECT språk, kunskapnivå from Språkkunskap WHERE guide=?";
		List<LanguageSkill> skills = new ArrayList<>();
		String persnr = guide.getPersnr();
		try {
			PreparedStatement stmnt = conn.prepareStatement(query);
			stmnt.setString(1, persnr);
			ResultSet rs = stmnt.executeQuery();
			while (rs.next()) {
				String lang = rs.getString(1);
				String level = rs.getString(2);
				skills.add(new LanguageSkill(lang, level));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return skills;
	}

	public void deleteLanguageSkill(Guide guide, LanguageSkill langSkill) {
		String query = "DELETE from Språkkunskap WHERE guide=? AND språk=?";
		try {
			PreparedStatement stmnt = conn.prepareStatement(query);
			stmnt.setString(1, guide.getPersnr());
			stmnt.setString(2, langSkill.getLanguage());
			stmnt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<String> findAllLanguages() {
		String query = "SELECT * FROM Språk";
		Statement stmnt;
		List<String> languagages = new ArrayList<>();
		try {
			stmnt = conn.createStatement();
			ResultSet rs = stmnt.executeQuery(query);
			while (rs.next()) {
				languagages.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return languagages;

	}

	public List<String> findUnregistredLanguages(Guide guide) {
		String query = "SELECT * FROM Språk WHERE namn NOT IN (SELECT språk FROM Språkkunskap WHERE guide=?) ORDER BY namn";
		List<String> languagages = new ArrayList<>();
		try {
			PreparedStatement stmnt = conn.prepareStatement(query);
			stmnt.setString(1, guide.getPersnr());
			ResultSet rs = stmnt.executeQuery();
			while (rs.next()) {
				languagages.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return languagages;
	}

	public void createNewLanguageSkill(Guide guide, LanguageSkill skill) {
		String query = "INSERT INTO Språkkunskap (guide, språk, kunskapnivå) VALUES (?, ?, ?)";
		try {
			PreparedStatement stmnt = conn.prepareStatement(query);
			stmnt.setString(1, guide.getPersnr());
			stmnt.setString(2, skill.getLanguage());
			stmnt.setString(3, skill.getLevel());
			stmnt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
