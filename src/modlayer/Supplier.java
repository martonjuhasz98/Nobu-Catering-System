package modlayer;

public class Supplier {

	private String cvr;
	private String name;
	private String address;
	private City city;
	private String phone;
	private String email;
	
	public Supplier() {
		
	}

	public String getCvr() {
		return cvr;
	}
	public String getName() {
		return name;
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
	public void setCvr(String cvr) {
		this.cvr = cvr;
	}
	public void setName(String name) {
		this.name = name;
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
}
