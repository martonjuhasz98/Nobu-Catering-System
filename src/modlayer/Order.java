package modlayer;

import java.util.ArrayList;

public class Order {

	private int id;
	private int tableNo;
	private Employee employee;
	private Transaction transaction;
	private ArrayList<OrderMenuItem> items;
	
	public Order() {
		id = -1;
		tableNo = -1;
	}

	public int getId() {
		return id;
	}
	public int getTableNo() {
		return tableNo;
	}
	public Employee getEmployee() {
		return employee;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public ArrayList<OrderMenuItem> getItems() {
		return items;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public void setItems(ArrayList<OrderMenuItem> items) {
		this.items = items;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof Order)) {
	      return false;
	    }
	    Order i = (Order)o;
	    return i.id == id;
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
