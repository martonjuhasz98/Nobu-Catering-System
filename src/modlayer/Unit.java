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
	}@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof Unit)) {
	      return false;
	    }
	    Unit i = (Unit)o;
	    return i.abbr.equals(abbr);
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
