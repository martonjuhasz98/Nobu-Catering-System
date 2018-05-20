package modlayer;

public class Unit {

	private String abbr;
	private String name;
	
	public Unit() {
		
	}
	public Unit(String abbr, String name) {
		super();
		this.abbr = abbr;
		this.name = name;
	}

	public String getAbbr() {
		return abbr;
	}
	public String getName() {
		return name;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return abbr;
	}
}
