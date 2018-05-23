package modlayer;

public class City {

	private String zipCode;
	private String name;
	
	public City() {
		
	}
	public City(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getZipCode() {
		return zipCode;
	}
	public String getName() {
		return name;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof City)) {
	      return false;
	    }
	    City c = (City)o;
	    return c.zipCode.equals(zipCode);
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
