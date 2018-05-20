package modlayer;

public class Order {

	private int id;
	private int tableNo;
	private Employee employee;
	private Payment payment;
	
	public Order() {
		super();
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
	public Payment getPayment() {
		return payment;
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
	public void setPayment(Payment payment) {
		this.payment = payment;
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
