package modlayer;

import java.util.Date;

public class Session {

	private Date date;
	private String employee;
	
	public Session() {
	}

	public Date getDate() {
		return date;
	}
	public String getEmployee() {
		return employee;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
}
