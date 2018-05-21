package modlayer;

public class Employee {

	private String cpr;
	private String name;
	private String username;
	private String password;
	private String address;
	private City city;
	private String phone;
	private String email;
	private int accessLevel;
	
	public Employee() {
		accessLevel = -1;
	}

	public String getCpr() {
		return cpr;
	}
	public String getName() {
		return name;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getAddress() {
		return address;
	}
	public City getCity() {
		return city;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	public int getAccessLevel() {
		return accessLevel;
	}
	public void setCpr(String cpr) {
		this.cpr = cpr;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof Employee)) {
	      return false;
	    }
	    Employee e = (Employee)o;
	    return e.cpr.equals(cpr);
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
