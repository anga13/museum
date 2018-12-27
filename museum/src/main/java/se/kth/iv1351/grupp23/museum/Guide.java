package se.kth.iv1351.grupp23.museum;

public class Guide {

	private String persnr;
	private String enamn;
	private String fnamn;

	public Guide(String persnr, String enamn, String fnamn) {
		this.persnr = persnr;
		this.enamn = enamn;
		this.fnamn = fnamn;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("%s: %s %s", persnr, fnamn, enamn);
	}

	public String getPersnr() {
		// TODO Auto-generated method stub
		return this.persnr;
	}

	public String getEnamn() {
		// TODO Auto-generated method stub
		return this.enamn;
	}

	public String getFnamn() {
		// TODO Auto-generated method stub
		return this.fnamn;
	}

}
