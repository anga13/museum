package se.kth.iv1351.grupp23.museum;

public class LanguageSkill {
	private String language;
	private String level;
	
	public LanguageSkill(String language, String level) {
		this.language = language;
		this.level = level;
	}
	public String getLanguage() {
		return language;
	}
	public String getLevel() {
		return level;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("%s: %s", language, level);
	}
}
