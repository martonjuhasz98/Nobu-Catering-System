package modlayer;

import java.util.Date;

public class Stocktaking {

	private int id;
	private Date timestamp;
	private Employee employee;
	
	public Stocktaking() {
		id = -1;
	}

	public int getId() {
		return id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof Stocktaking)) {
	      return false;
	    }
	    Stocktaking i = (Stocktaking)o;
	    return i.id == id;
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
