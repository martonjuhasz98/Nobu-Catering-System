package modlayer;

import java.util.ArrayList;
import java.util.Date;

public class Stocktaking {

	private int id;
	private Date timestamp;
	private Employee employee;
	private ArrayList<Discrepancy> discrepancies;
	
	public Stocktaking() {
		id = -1;
		discrepancies = new ArrayList<Discrepancy>();
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
	public ArrayList<Discrepancy> getDiscrepancies() {
		return discrepancies;
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
	public void setDiscrepancies(ArrayList<Discrepancy> discrepancies) {
		this.discrepancies = discrepancies;
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
