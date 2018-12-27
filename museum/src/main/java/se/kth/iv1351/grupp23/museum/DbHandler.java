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
	private String URL = "jdbc:sqlite:/home/anders/school/database/museum.db";
	private String driver = "org.sqlite.JDBC";
	private String userID = "";
	private String password = "";
	
	public void connect() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(URL);
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
}
